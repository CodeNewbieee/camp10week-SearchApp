package com.example.imagesearchapp.Retrofit

import retrofit2.http.GET
import retrofit2.http.Query

const val KAKAO_API_KEY = "3ea651ffcbc1122621375790dc07d854"
interface KakaoAPI {
    @GET("image")
    suspend fun getSearchImage (
        @Query("Authorization") Authorization : String = KAKAO_API_KEY,
        @Query("query") query : String,
        @Query("sort") sort : String = "recency",
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 80
    ) : SearchData
}