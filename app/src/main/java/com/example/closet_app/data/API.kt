package com.example.closet_app.data

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.POST

interface API {
    interface RetrofitAPI {
        @POST("/predict2")//서버에 GET요청을 할 주소를 입력
        fun postPredict() : Call<JsonObject> //MainActivity에서 사용할 json파일 가져오는 메서드
    }
}