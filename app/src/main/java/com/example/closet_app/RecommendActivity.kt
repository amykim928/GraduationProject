package com.example.closet_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.example.closet_app.data.API
import com.example.closet_app.data.DataModel
import com.example.closet_app.data.ImgDataModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class RecommendActivity : AppCompatActivity() {
    lateinit var mRetrofit: Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: API.RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var mCallImgList: Call<ImgDataModel> // Json형식의 데이터를 요청하는 객체입니다.
    lateinit var imageView: ImageView
    //detectActivity에서 ArrayList<Uri>를 건네주는 식으로 바꾸려고 하네요.
    val bit = ArrayList<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        setRetrofit()
        val gohome: Button = findViewById<View>(R.id.gohome) as Button
        imageView=findViewById(R.id.recommendImg)
        val gSatisfaction: RadioGroup = findViewById<View>(R.id.gSatisfaction) as RadioGroup
        val saveSatisfaction: Button = findViewById<View>(R.id.saveSatisfaction) as Button
        val saveResult: Button = findViewById<View>(R.id.saveResult) as Button
        val retry: Button = findViewById<View>(R.id.retry) as Button
        val getCloset: Button = findViewById<View>(R.id.getCloth) as Button

        saveSatisfaction.setOnClickListener {
            Toast.makeText(this, "만족도를 저장했습니다!", Toast.LENGTH_SHORT).show()
            gSatisfaction.clearCheck()
        }

        saveResult.setOnClickListener {
            //val myIntent = Intent(this, ClosetActivity::class.java)
            Toast.makeText(this, "결과를 저장했습니다!", Toast.LENGTH_SHORT).show()

        }

        retry.setOnClickListener {
            //val myIntent = Intent(this, ClosetActivity::class.java)
            //startActivity(myIntent)
        }
        // 이걸로 내부 저장소에 있는 옷을 가져와서 api에 보내보죠.
        getCloset.setOnClickListener {
            val file = File(filesDir.toString())
            val files = file.listFiles()
        //    Log.i("first","maybehere")
            for (tempFile in files!!) {
                val path = filesDir.toString() + "/" + tempFile.name
                bit.add(BitmapFactory.decodeFile(path))
            }

        //    Log.i("second","maybehere")
            val bitString = bitmapToString(bit[0])
         //   Log.i("third","maybehere")
            mCallImgList = mRetrofitAPI.postImgPredict(bitString)  // RetrofitAPI에서 Json객체 요청을 반환하는 메서드를 불러옵니다.
        //    Log.i("fourth","maybehere")
            mCallImgList.enqueue(mRetrofitCallback)
            //imageView.setImageBitmap(bit[5])
        }

        gohome.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }

        val colorOption = findViewById<TextView>(R.id.colorOption)
        val styleOption = findViewById<TextView>(R.id.styleOption)
        colorOption.text = intent.getStringExtra("color")
        styleOption.text = intent.getStringExtra("style")


        //해야할 것->내부 저장소에서 의상 불러오기
        //의상을 불러와  base64로 바꾸고, json으로 파싱해서 서버로 보내고
        //서버(모델)에서 값을 받아옴
        //
//        mCallTodoList = mRetrofitAPI.postPredict()
//        mCallTodoList.enqueue(mRetrofitCallback) // 콜백, 즉 응답들을 큐에 넣어 대기시켜놓습니다. 응답이 생기면 뱉어내는거죠.
    }

    private fun setRetrofit() {
        //레트로핏으로 가져올 url설정하고 세팅
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(API.RetrofitAPI::class.java)
    }

    //http요청을 보냈고 이건 응답을 받을 콜벡메서드
    private val mRetrofitCallback = (object : retrofit2.Callback<ImgDataModel> {
        override fun onResponse(call: Call<ImgDataModel>, response: Response<ImgDataModel>) {
            val result = response.body()
            val img0=stringToBitmap(result!!.img0)
            val img1=stringToBitmap(result.img1)
            val img2=stringToBitmap(result.img2)
            val img3=stringToBitmap(result.img3)
            val img4=stringToBitmap(result.img4)
            val img5=stringToBitmap(result.img5)
            val img6=stringToBitmap(result.img6)
            val img7=stringToBitmap(result.img7)
            bit.add(img0)
            bit.add(img1)
            bit.add(img2)
            bit.add(img3)
            bit.add(img4)
            bit.add(img5)
            bit.add(img6)
            bit.add(img7)
            imageView.setImageBitmap(img1)
            saveBitmap(img1,1.toString())
            Log.i("fifth","maybehere")
            Log.i("bits?:", bit[1].height.toString())
           // Toast.makeText(this@RecommendActivity,"추천 완료",Toast.LENGTH_SHORT).show()


        }

        override fun onFailure(call: Call<ImgDataModel>, t: Throwable) {
            t.printStackTrace()
            Log.i("failureT",t.message.toString())
        }


    })//Json객체를 응답받는 콜백 객체

    private fun saveBitmap(bitmap: Bitmap, name: String) {

        //내부저장소 캐시 경로를 받아옵니다.
        //지금은 그냥 filesdir로 했지만,
        //나중에는 dir에 디렉토리를 만들어서 /files/cropped/1.jpg 이런식으로 저장하는게 좋겟죠.

        val storage=filesDir
        val filename= "saved$name.jpg"

        val tmpFile=File(storage,filename)
        tmpFile.createNewFile()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(tmpFile))

    }
    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}



// val bitmap = stringToBitmap("여기에 인코딩된 이미지 string")
//
//image.setImageBitmap(bitmap)

//    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, "여기에 이미지 uri")
//
//    bitmapToString(bitmap)
