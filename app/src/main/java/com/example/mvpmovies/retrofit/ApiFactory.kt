package com.example.mvpmovies.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory private constructor(
    private val retrofit: Retrofit =  Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .baseUrl(BASE_URL)
    .build()) {

    companion object {
        private var apiFactory: ApiFactory? = null
        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        fun getInstance(): ApiFactory?{
            if (apiFactory==null)
                apiFactory = ApiFactory()
            return apiFactory
        }
    }
    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}