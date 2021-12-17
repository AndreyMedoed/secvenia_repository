package com.example.networking.essences


data class Genre(
    val name: String,
    var isSelected: Boolean
) : Item()