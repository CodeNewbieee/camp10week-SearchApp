package com.example.imagesearchapp.Retrofit

import com.example.imagesearchapp.SearchFragment
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoAPI {
    @GET("image")
    suspend fun getSearchImage (
        @Query("query") query : String = "",
        @Query("sort") sort : String = "recency",
        @Query("page") page : Int = 1,
        @Query("size") size : Int = 80
    ) : SearchData
}