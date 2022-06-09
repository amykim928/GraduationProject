package com.example.closet_app

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.closet_app.databinding.ActivityDetectBinding
import com.example.closet_app.tracker.MultiBoxTracker
import com.example.closet_app.classfier.YoloClassfier
import com.example.graduateproject.classfiers.YoloInterfaceClassfier
import com.example.graduateproject.env.ImageUtils
import com.example.graduateproject.env.Utils
import java.io.*


class DetectActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetectBinding

    //초기화면 및 화면에 보이는 사진을 bitmap으로 사용합니다.
    //bitmap을 imageView에 보이게할 때 사용합니다.
    lateinit var bitmap: Bitmap

    //사진 사이즈를 416으로 정하겠다는 의미입니다.
    val TF_OD_API_INPUT_SIZE = 416

    //의상 검출을 위한 변수
    lateinit var detector: YoloClassfier

    //특정 구역이 40%이상의 확률로 특정 카테고리로 판정할 때 쓰는 변수
    val MINIMUM_CONFIDENCE_TF_OD_API=0.4f


    //의상 검출 결과 변수 저장
    lateinit var resultList : List<YoloInterfaceClassfier.Recognition>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initbox()
    }

    //가로 세로 길이용 변수
    protected var previewWidth = 0
    protected var previewHeight = 0


    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    lateinit var tracker: MultiBoxTracker

    //이미지 잘라내기를 위한 변수인데,
    //이미지를 바로 잘라내지 않고 나중에 잘라내기 위해 false로 했습니다.
    private val MAINTAIN_ASPECT = false

    //회전과 관련된 변수
    private val sensorOrientation = 90

    //양자화
    //모델을 가볍게 쓰기 위해 양자화를 쓰는 경우도 있긴한데, 전 안씁니다.
    private val TF_OD_API_IS_QUANTIZED = false

    //나중에 모델 더 좋게 학습하면 모델이름을 바꾸거나 업데이트하겠죠.
    //모델 이름과 경로, obj.txt는 이미지의 카테고리입니다(셔츠, 팬츠 같은)
    private val TF_OD_API_MODEL_FILE = "yolov4_2.tflite"

    private val TF_OD_API_LABELS_FILE = "file:///android_asset/obj.txt"
    //검출하는 부분은 여기다가 두었습니다.

    private fun initbox() {
        previewHeight = 416
        previewWidth = 416

        //matrix 416x416 행렬이 생겼다 정도로 이해하면 될듯합니다.
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
            sensorOrientation, MAINTAIN_ASPECT
        )

        cropToFrameTransform = Matrix()
        //역행렬 찾음 cropToFrameTramsForm에.
        frameToCropTransform!!.invert(cropToFrameTransform)
        
        
        //activity Detect에, trackingOverlay가 있는데, 
        //handleResult에 canvas.drawRect(location, paint)의 주석을 지우면 아래가 작동하여, 네모를 그립니다.
        tracker = MultiBoxTracker(this)
        binding.trackingOverlay.addCallback { canvas: Canvas -> tracker.draw(canvas) }

        //기본 설정입니다.
        tracker.setFrameConfiguration(
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_INPUT_SIZE,
            sensorOrientation
        )
        try {
            Log.i("main fail :","check")
            //YoloDetector를 만드는 부분입니다. 416x416 사이즈에, 양자화를 사용하지 않는다- 라는 걸 넘겨줍니다.
            //asset은 학습한 모델의 경로를 알기 위해 넘겨줍니다.
            detector = YoloClassfier().create(
                assets,
                TF_OD_API_MODEL_FILE,
                TF_OD_API_LABELS_FILE,
                TF_OD_API_IS_QUANTIZED
            )

        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("main fail :","Exception initializing classifier!")
            Toast.makeText(
                applicationContext, "Classifier could not be initialized", Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    
    fun init(){
        val imageView=binding.thisPhoto
        val detectBtn=binding.detectButton
        val getImgBtn=binding.getImgButton
        val saveBtn=binding.saveButton
        val cropBtn=binding.cropImgButton

        // 기본 이미지 .267.jpg를 가져옵니다. (asset에 기본 파일을 저장함)
        //setImageBitmap을통해 416x416 size로 가져옵니다.
        val source = Utils.getBitmapFromAsset(this@DetectActivity, "267.jpg")
        bitmap= source?.let { Utils.processBitmap(it,TF_OD_API_INPUT_SIZE) }!!
        imageView.setImageBitmap(source.let { Utils.processBitmap(it,TF_OD_API_INPUT_SIZE) })


        //startActivityforResult가 현재 지원 종료상태라, registerForActivityResult로 바꾸어봤습니다.
        //이미지를 얻을 수 있는 곳으로 이동합니다.
        getImgBtn.setOnClickListener {
            Toast.makeText(this,"사진 변경",Toast.LENGTH_SHORT).show()
            //상단의 getcontent로 이동합니다.
            getContent.launch("image/*")
        }
        //옷장으로 돌아가는 액티비티
        saveBtn.setOnClickListener {
            Toast.makeText(this,"옷장으로 이동.",Toast.LENGTH_SHORT).show()

            val myIntent= Intent(this,ClosetActivity::class.java)
            startActivity(myIntent)
            //finish()를 달면, back버튼을 눌러도 detectactivity로 돌아오지 않습니다.
            //인텐트만을 이용하면 해당 액티비티가 꺼지지 않습니다. 직접 finish를 이용해 꺼야해요.
            finish()

        }

        //탐지 버튼
        detectBtn.setOnClickListener{
            Toast.makeText(this,"의상 검출",Toast.LENGTH_SHORT).show()

            val handler = Handler(Looper.getMainLooper())
            //비동기적으로
            Thread {
                //의상 탐지를 위해 detector에 bitmap이미지를 넣습니다.
                val results: List<YoloInterfaceClassfier.Recognition>? =
                    detector.recognizeImage(bitmap)

                handler.post(Runnable {
                    //handleresult가 옷의 위치를 그리는 함수입니다.
                    if (results != null) {
                        //resultlist에 result를 저장하여, 이미지에 대한 정보를 저장하도록 합니다.
                        resultList=handleResult(bitmap, results)
                    }
                })
            }.start()
        }

        //잘라내기 버튼입니다.
        cropBtn.setOnClickListener {

            val cropList= arrayListOf<Bitmap>()
            val subResult = arrayListOf<YoloInterfaceClassfier.Recognition>()

            //이미지를 검출한 것을 crop(잘라내서) 의상으로 저장할 것입니다
            for (result in resultList){
                val location=result.location
                if (location != null && result.confidence!! >= MINIMUM_CONFIDENCE_TF_OD_API) {
                    val cropBitmap=cropBitmaps(bitmap,result.location!!)
                    cropList.add(cropBitmap)
                    subResult.add(result)
                }

            }
            Toast.makeText(this,"의상 저장", Toast.LENGTH_SHORT).show()


            //python의 enumerate고,각각 사진과 사진의 정보를 함수를 통해 저장합니다.
            for((idx,crops) in cropList.withIndex()){
                saveBitmap(crops,idx.toString())
            }

            for((idx,results) in subResult.withIndex()){
                saveResult(results,idx.toString())
            }


        }


    }


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 이미지의 uri값을 활용해 main activity의 이미지 뷰 변경
        binding.thisPhoto.setImageURI(uri)
        //비트맵을 uri에서 가져오는데, isMutableRequired를 설정해주기 위해 Listener를 하나 만들었습니다.
        // (비트맵을 변경가능하게 해주는 옵션)
        bitmap=
            Utils.processBitmap(
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver,uri!!),
                    ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }),TF_OD_API_INPUT_SIZE)

        //가져온 이미지를 이미지뷰에 세팅
        binding.thisPhoto.setImageBitmap(bitmap)
    }

    //좌표와 높이너비를 받아 잘라냄
    fun cropBitmaps(original :Bitmap,location:RectF):Bitmap{
        val x1 :Int = location.left.toInt()
        val y1 :Int = location.top.toInt()
        val width:Int =location.width().toInt()
        val height : Int =location.height().toInt()
       // Log.i("xy2 : ", "$x1/$y1/${location.width()}/${location.height()}")

        return Bitmap.createBitmap(original,x1,y1,width, height)

    }
    private fun handleResult(bitmap: Bitmap, results: List<YoloInterfaceClassfier.Recognition>): List<YoloInterfaceClassfier.Recognition> {
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f

        for (result in results) {

            val location: RectF? = result.location

            if (location != null && result.confidence!! >= MINIMUM_CONFIDENCE_TF_OD_API) {
                //여기있는 location이 옷이 있는 좌표입니다. 그것을 활용해서 옷의 위치를 추출해주시면 될 것 같습니다
                Log.i("results",location.toShortString()+result.title)
              //  Log.i("xy : ", "${location.left}/${location.top}/${location.width()}/${location.height()}")
              //  canvas.drawRect(location, paint)
            }
        }
        return results
    }

    private fun saveBitmap(bitmap: Bitmap, name: String) {

        //내부저장소  경로를 받아옵니다. filesDir
        //save 디렉토리를 만들어서 /files/save/1.jpg 이런식으로 저장하는게 좋겟죠.
        val dirs = File("$filesDir/save")
        if (!dirs.exists()){
            dirs.mkdirs()
        }
        if(dirs.isDirectory) {	Log.i("dir", "디렉토리입니다..: $filesDir/save")}

        //비트맵 저장입니다.
        val file = File("$filesDir/save/$name.jpg")
        file.createNewFile()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,FileOutputStream(file))

    }
    private fun saveResult(results: YoloInterfaceClassfier.Recognition, name:String){
        //텍스트 저장입니다.
        val file2=File("$filesDir/save/$name.txt")
        val printWriter= FileWriter(file2)
        val buffer= BufferedWriter(printWriter)
        buffer.write(results.title)
        buffer.close()

    }
}