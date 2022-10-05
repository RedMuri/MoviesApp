package com.example.mvpmovies.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseVideo(
    val results: ArrayList<Video>
)