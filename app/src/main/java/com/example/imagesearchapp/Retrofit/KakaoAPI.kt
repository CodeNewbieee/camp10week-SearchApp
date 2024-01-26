package com.example.imagesearchapp.Retrofit

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val KAKAO_API_KEY = "KakaoAK 3ea651ffcbc1122621375790dc07d854"
interface KakaoAPI {
    @GET("image")
    suspend fun getSearchImage (
        @Header("Authorization") Authorization : String = KAKAO_API_KEY,
        @Query("query") query : String
    ) : SearchData
}