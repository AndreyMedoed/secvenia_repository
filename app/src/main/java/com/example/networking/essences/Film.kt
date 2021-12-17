package com.example.networking.essences

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Film(
    @Json(name = "id")
    val id: Int,
    @Json(name = "type")
    val type: String?,
    @Json(name = "enName")
    val enName: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "poster")
    val poster: Poster,
    @Json(name = "year")
    val year: Int?,
    @Json(name = "rating")
    val rating: Rating?,
    @Json(name = "description")
    val description: String?,
) : Item(), Parcelable