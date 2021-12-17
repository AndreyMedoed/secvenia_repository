package com.example.networking.network

import com.example.networking.CustomInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object NetworkConfig {
    private const val TOKEN = "FGQGZD5-C7545AF-QAXZ00Z-30NA1RJ"


    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        ).addInterceptor(CustomInterceptor(TOKEN))
        .build()

    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.kinopoisk.dev/movie/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    val unsplashApi: UnsplashApi
        get() = retrofit.create()

}