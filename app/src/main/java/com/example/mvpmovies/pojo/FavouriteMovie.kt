package com.example.mvpmovies.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouriteMovies")
data class FavouriteMovie(
    @PrimaryKey
    var id:Int,
    var overview: String,
    var popularity: Float,
    var posterPath: String,
    var releaseDate: String,
    var title: String,
    var voteAverage: Float,
    var voteCount: Int
) {
    constructor(movie: Movie) : this(
        movie.id,
        movie.overview,
        movie.popularity,
        movie.posterPath,
        movie.releaseDate,
        movie.title,
        movie.voteAverage,
        movie.voteCount)
}