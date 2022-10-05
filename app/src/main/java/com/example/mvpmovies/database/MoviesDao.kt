package com.example.mvpmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>
    @Query("SELECT * FROM movies WHERE id == :movieId ")
    fun getMovieById(movieId:Int):Single<Movie?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>): Completable
    @Delete
    fun delete(movie: Movie): Completable
    @Query("DELETE FROM movies")
    fun deleteAll(): Completable

    @Query("SELECT * FROM favouriteMovies")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>
    @Query("SELECT * FROM favouriteMovies WHERE id == :favouriteMovieId ")
    fun getFavouriteMovieById(favouriteMovieId:Int):Single<FavouriteMovie?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favouriteMovies: List<FavouriteMovie>): Completable
    @Delete
    fun deleteFavourite(favouriteMovie: FavouriteMovie): Completable
    @Query("DELETE FROM favouriteMovies")
    fun deleteAllFavourite(): Completable
}