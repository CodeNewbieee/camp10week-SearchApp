package com.example.imagesearchapp.Retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchRetrofit {
    private const val BASE_URL = "https://dapi.kakao.com/v2/search/"

    val api: KakaoAPI by lazy { retrofit.create(KakaoAPI::class.java) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
            ) // 개발 도중에는 필수로 넣고, 배포시 삭제할 것!
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}