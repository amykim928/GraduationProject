package com.example.closet_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.closet_app.data.API
import com.example.closet_app.data.DataModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class RecommendActivity : AppCompatActivity() {
    lateinit var mRetrofit :Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: API.RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var mCallTodoList : retrofit2.Call<JsonObject> // Json형식의 데이터를 요청하는 객체입니다.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        val gohome: Button = findViewById<View>(R.id.gohome) as Button

        val gSatisfaction: RadioGroup = findViewById<View>(R.id.gSatisfaction) as RadioGroup
        val saveSatisfaction: Button = findViewById<View>(R.id.saveSatisfaction) as Button
        val saveResult: Button = findViewById<View>(R.id.saveResult) as Button
        val retry: Button = findViewById<View>(R.id.retry) as Button
        val goCloset: Button = findViewById<View>(R.id.getCloth) as Button

        saveSatisfaction.setOnClickListener{
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

        goCloset.setOnClickListener {
            val myIntent = Intent(this, ClosetActivity::class.java)
            startActivity(myIntent)
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
        //mCallTodoList = mRetrofitAPI.postPredict() // RetrofitAPI에서 Json객체 요청을 반환하는 메서드를 불러옵니다.
        mCallTodoList.enqueue(mRetrofitCallback) // 콜백, 즉 응답들을 큐에 넣어 대기시켜놓습니다. 응답이 생기면 뱉어내는거죠.
    }

    private fun setRetrofit(){
        //레트로핏으로 가져올 url설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(API.RetrofitAPI::class.java)
    }

    //http요청을 보냈고 이건 응답을 받을 콜벡메서드
    private val mRetrofitCallback  = (object : retrofit2.Callback<JsonObject>{//Json객체를 응답받는 콜백 객체

        //응답을 가져오는데 실패
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d("TAG", "에러입니다. => ${t.message.toString()}")

        }
        //응답을 가져오는데 성공 -> 성공한 반응 처리
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            val result = response.body()
            Log.d("TAG", "결과는 => $result")

            var mGson = Gson()
            val dataParsed1 = mGson.fromJson(result, DataModel.img0::class.java)
            val dataParsed2 = mGson.fromJson(result, DataModel.img1::class.java)
            val dataParsed3 = mGson.fromJson(result, DataModel.img2::class.java)

        }
    })

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



// val bitmap = stringToBitmap("여기에 인코딩된 이미지 string")
//
//image.setImageBitmap(bitmap)

//    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, "여기에 이미지 uri")
//
//    bitmapToString(bitmap)
}