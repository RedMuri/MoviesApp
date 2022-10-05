package com.example.mvpmovies.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video(
    val name: String,
    val key: String,
    @SerializedName("published_at")
    @Expose
    val data: String
)
