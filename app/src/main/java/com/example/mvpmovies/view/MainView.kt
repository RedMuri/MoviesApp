package com.example.mvpmovies.view

import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie
import com.example.mvpmovies.pojo.Review
import com.example.mvpmovies.pojo.Video

interface MainView {
    fun onError(throwable: Throwable)
    fun onSuccessMovie(movies: List<Movie>)
    fun onSuccessFavouriteMovie(favouriteMovies: List<FavouriteMovie>)
    fun onErrorFavouriteMovie()
    fun onSuccessVideos(videos: List<Video>)
    fun onErrorVideos(message: String?)
    fun onSuccessReviews(reviews: List<Review>)
    fun onErrorReviews(message: String?)
}