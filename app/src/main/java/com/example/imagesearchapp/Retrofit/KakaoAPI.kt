package com.example.imagesearchapp.Retrofit

import com.example.imagesearchapp.Constans
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface KakaoAPI {
    @GET("image")
    suspend fun getSearchImage (
        @Header("Authorization") Authorization : String = Constans.KAKAO_API_KEY,
        @Query("query") query : String
    ) : SearchData
}