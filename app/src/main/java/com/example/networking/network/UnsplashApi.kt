package com.example.networking.network


import com.example.networking.essences.Search
import retrofit2.Response
import retrofit2.http.*

interface UnsplashApi {

    @GET(".")
    suspend fun getFilmList(
        @Query("field") field: String = "genres.name",
        @Query("search") genre: String,
        @Query("sortField") sort_by: String = "name",
        @Query("sortType") sortType: String = "-1",
        @Query("page") page: String? = null,
        @Query("limit") pageSize: String
    ): Response<Search>
}