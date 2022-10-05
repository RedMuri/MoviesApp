package com.example.mvpmovies.pojo

import com.google.gson.annotations.SerializedName

data class Review(
    val author: String,
    val content: String,
    @SerializedName("created_at")
    val data: String
)
