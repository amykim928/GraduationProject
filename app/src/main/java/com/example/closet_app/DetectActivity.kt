package com.example.closet_app

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.closet_app.databinding.ActivityDetectBinding
import com.example.closet_app.tracker.MultiBoxTracker
import com.example.graduateproject.classfiers.YoloClassfier
import com.example.graduateproject.classfiers.YoloInterfaceClassfier
import com.example.graduateproject.env.ImageUtils
import com.example.graduateproject.env.Utils
import java.io.IOException

class DetectActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetectBinding
    lateinit var bitmap: Bitmap
    val TF_OD_API_INPUT_SIZE = 416

    //의상 검출을 위한 변수
    lateinit var detector: YoloClassfier
    var detection=false
    val MINIMUM_CONFIDENCE_TF_OD_API=0.5f
    
    //의상 검출 결과 변수 저장
    lateinit var result : List<YoloInterfaceClassfier.Recognition>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initbox()
    }

    protected var previewWidth = 0
    protected var previewHeight = 0
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    lateinit var tracker: MultiBoxTracker

    private val MAINTAIN_ASPECT = false
    private val sensorOrientation = 90

    private val TF_OD_API_IS_QUANTIZED = false

    //나중에 모델 더 좋게 학습하면 모델이름을 바꾸거나 업데이트하겠죠.
    private val TF_OD_API_MODEL_FILE = "yolov4_1.tflite"

    private val TF_OD_API_LABELS_FILE = "file:///android_asset/obj.txt"
    //검출하는 부분은 여기다가 두었습니다.

    private fun initbox() {
        previewHeight = 416
        previewWidth = 416
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
            sensorOrientation, MAINTAIN_ASPECT
        )

        cropToFrameTransform = Matrix()
        frameToCropTransform!!.invert(cropToFrameTransform)
        tracker = MultiBoxTracker(this)

        binding.trackingOverlay.addCallback { canvas: Canvas -> tracker.draw(canvas) }

        tracker.setFrameConfiguration(
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_INPUT_SIZE,
            sensorOrientation
        )
        try {
            Log.i("main fail :","check")
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

        // 기본 이미지 .267.jpg를 가져옵니다. (asset에 기본 파일을 저장함)
        val source = Utils.getBitmapFromAsset(this@DetectActivity, "267.jpg")
        bitmap= source?.let { Utils.processBitmap(it,TF_OD_API_INPUT_SIZE) }!!
        binding.thisPhoto.setImageBitmap(source.let { Utils.processBitmap(it,TF_OD_API_INPUT_SIZE) })

        //startActivityforResult가 현재 지원 종료상태라, registerForActivityResult로 바꾸어봤습니다.
        getImgBtn.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"사진 변경",Toast.LENGTH_SHORT).show()
            //상단의 getcontent로 이동합니다.
            getContent.launch("image/*")
        })

        saveBtn.setOnClickListener {
            Toast.makeText(this,"사진 저장후 옷장으로 이동",Toast.LENGTH_SHORT).show()
            
            val myIntent= Intent(this,ClosetActivity::class.java)
            startActivity(myIntent)
        }


        detectBtn.setOnClickListener{
            Toast.makeText(this,"의상 검출",Toast.LENGTH_SHORT).show()
            val handler = Handler(Looper.getMainLooper())

            Thread {
                //의상 탐지를 위해 handler를 위처럼 적어주시고, detector에 bitmap이미지를 넣되, 오류가 나면 49 line에 있는 listener를 bitmap에 쓰셔야합니다.
                val results: List<YoloInterfaceClassfier.Recognition>? =
                    detector.recognizeImage(bitmap)

                handler.post(Runnable {
                    //handleresult가 옷의 위치를 그리는 함수입니다. handresult를 변형하셔서, 위치를 가져오는게 좋아요.
                    if (results != null) {
                        result=handleResult(bitmap, results)
                    }
                })
            }.start()
        }

    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 이미지의 uri값을 활용해 main activity의 이미지 뷰 변경
        binding.thisPhoto.setImageURI(uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Toast.makeText(this,"Bitmap 얻어오기", Toast.LENGTH_SHORT).show()
            bitmap=
                Utils.processBitmap(
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(contentResolver,uri!!),
                        ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            decoder.isMutableRequired = true
                        }),TF_OD_API_INPUT_SIZE)
        }

        //sdk 버전이 낮으면 아래를 쓸 수 있는데 윗경우를 권장합니다.
        else{
            Toast.makeText(this,"Bitmap 얻어오기2", Toast.LENGTH_SHORT).show()
            bitmap= MediaStore.Images.Media.getBitmap(contentResolver,uri)
        }
        binding.thisPhoto.setImageBitmap(bitmap)
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

                Log.i("results",location.toShortString())
                canvas.drawRect(location, paint)
            }
        }
        return results
    }
}