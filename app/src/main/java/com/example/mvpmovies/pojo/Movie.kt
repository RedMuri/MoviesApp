package com.example.mvpmovies.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    var id: Int,

    var overview: String,

    var popularity: Float,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String,

    var title: String,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,
){
    constructor(movie: FavouriteMovie) : this(
        movie.id,
        movie.overview,
        movie.popularity,
        movie.posterPath,
        movie.releaseDate,
        movie.title,
        movie.voteAverage,
        movie.voteCount)
}
