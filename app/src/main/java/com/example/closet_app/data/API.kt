package com.example.closet_app.data

import com.google.gson.JsonObject
import org.json.JSONObject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

//Refroit2으로 python과 서버 통신을 위해
//get,Post,put delete를 구현해뒀다고 생각하시면 될 것 같습니다.
interface API {
    interface RetrofitAPI {

        // 주소://predict3으로 요청하는데, String(이미지를 스트링으로 변환했음)과 그 이미지의 Feature를 전해주면
        //Call<ImgLabelModel>로 반환해준다는 뜻입니다.
        //data에 제가 실험하느라 여러 datamodel이 보이실텐데, ImageFeatures와 ImgDataModel만 사용했습니다.
        @POST("/predict3")//서버에 GET요청을 할 주소를 입력
        fun postPredict(@Body img:HashMap<String,ImageFeatures>) : Call<ImgLabelModel> //RecommendActivity에서 사용할 json파일 가져오는 메서드

        @POST("/predict2")
        fun postImgPredict(@Body img:String) :Call<ImgDataModel>
    }

}