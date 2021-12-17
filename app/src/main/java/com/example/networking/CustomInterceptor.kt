package com.example.networking

import okhttp3.Interceptor
import okhttp3.Response

/* Этот интерцептор перехватывает request и и добавляет к url один параметр, и передает запрос дальше*/
class CustomInterceptor(private val TOKEN: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val originalUrl = originalRequest.url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("token", TOKEN)
            .build()
        val modifiedRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        val response = chain.proceed(modifiedRequest)
        return  response
    }

}