package com.example.imagesearchapp.Retrofit

import com.example.imagesearchapp.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val KAKAO_API_KEY = BuildConfig.API_KEY
interface KakaoAPI {
    @GET("image")
    suspend fun getSearchImage (
        @Header("Authorization") Authorization : String = KAKAO_API_KEY,
        @Query("query") query : String
    ) : SearchData
}