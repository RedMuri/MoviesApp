package com.example.mvpmovies.retrofit

import com.example.mvpmovies.pojo.ResponseMovie
import com.example.mvpmovies.pojo.ResponseReview
import com.example.mvpmovies.pojo.ResponseVideo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("{type}")
    fun getMovies(@Path("type") type: String,@Query("api_key") apiKey: String,@Query("language") lan: String,@Query("page") page: Int): Single<ResponseMovie>
    @GET("{id}/videos?")
    fun getVideos(@Path("id") id: Int, @Query("api_key") apiKey: String, @Query("language") lan: String): Single<ResponseVideo>
    @GET("{id}/reviews?")
    fun getReviews(@Path("id") id: Int, @Query("api_key") apiKey: String, @Query("language") lan: String,@Query("page") page: Int): Single<ResponseReview>
}