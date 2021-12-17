package com.example.networking.essences

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "docs")
    val filmList: List<Film>
)