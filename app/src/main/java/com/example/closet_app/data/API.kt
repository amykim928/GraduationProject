package com.example.closet_app.data

import com.google.gson.JsonObject
import org.json.JSONObject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    interface RetrofitAPI {
        @POST("/predict3")//서버에 GET요청을 할 주소를 입력
        fun postPredict(@Body img:HashMap<String,ImageFeatures>) : Call<ImgLabelModel> //RecommendActivity에서 사용할 json파일 가져오는 메서드

        @POST("/predict2")
        fun postImgPredict(@Body img:String) :Call<ImgDataModel>
    }

}