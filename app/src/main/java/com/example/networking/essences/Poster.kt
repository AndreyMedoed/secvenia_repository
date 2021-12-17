package com.example.networking.essences

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Poster(
    @Json(name = "url")
    val url: String,
    @Json(name = "previewUrl")
    val previewUrl: String
) : Parcelable