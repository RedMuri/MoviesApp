package com.example.mvpmovies.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ResponseMovie(
    @SerializedName("page")
    @Expose
    val page: Int,

    @SerializedName("results")
    @Expose
    val movies: ArrayList<Movie>
)
