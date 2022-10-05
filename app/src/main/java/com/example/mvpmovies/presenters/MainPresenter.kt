package com.example.mvpmovies.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mvpmovies.database.MovieDatabase
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie
import com.example.mvpmovies.retrofit.ApiFactory
import com.example.mvpmovies.utils.Utils
import com.example.mvpmovies.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val view: MainView, private val database: MovieDatabase) {
    private val compositeDisposable = CompositeDisposable()

    fun loadData(type: String, page: Int) {
        val apiFactory = ApiFactory.getInstance()
        val apiService = apiFactory?.getApiService()
        apiService?.let {
            compositeDisposable.add(apiService.getMovies(type,
                Utils.API_KEY,
                Utils.RUS,
                page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onSuccessMovie(it.movies)
                }, {
                    view.onError(it)
                }))
        }
    }

    fun loadVideos(movieId: Int) {
        val apiFactory = ApiFactory.getInstance()
        val apiService = apiFactory?.getApiService()
        apiService?.let {
            compositeDisposable.add(apiService.getVideos(movieId,
                Utils.API_KEY,
                Utils.RUS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isNotEmpty()) {
                        view.onSuccessVideos(it.results)
                    } else
                        view.onErrorVideos("Empty")
                }, {
                    view.onErrorVideos(it.message)
                }))
        }
    }

    fun loadReviews(movieId: Int) {
        val apiFactory = ApiFactory.getInstance()
        val apiService = apiFactory?.getApiService()
        apiService?.let {
            compositeDisposable.add(apiService.getReviews(movieId,
                Utils.API_KEY,
                Utils.RUS, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isNotEmpty()) {
                        view.onSuccessReviews(it.results)
                    } else
                        view.onErrorReviews("Empty")
                }, {
                    view.onErrorReviews(it.message)
                }))
        }
    }

    fun getAllMovies(): LiveData<List<Movie>> = database.moviesDao().getAllMovies()

    fun getMovieById(movieId: Int) {
        compositeDisposable.add(database.moviesDao().getMovieById(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null) {
                    view.onSuccessMovie(listOf(it))
                }
            }, {
                view.onError(it)
            }))
    }

    fun insertMovie(movies: List<Movie>) {
        compositeDisposable.add(database.moviesDao().insert(movies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.i("muri", "dbpr: ${it.message}")
            }))
    }

    fun deleteMovie(movie: Movie) {
        compositeDisposable.add(database.moviesDao().delete(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }) {
                Log.i("muri", "dbpr: ${it.message}")
            })
    }

    fun deleteAllMovies() {
        compositeDisposable.add(database.moviesDao().deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.i("muri", "dbpr: ${it.message}")
            }))
    }

    fun deleteAllMoviesRX() = database.moviesDao().deleteAll()
    fun disposeDisposable() = compositeDisposable.dispose()


    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>> =
        database.moviesDao().getAllFavouriteMovies()

    fun getFavouriteMovieById(favouriteMovieId: Int) {
        compositeDisposable.add(database.moviesDao().getFavouriteMovieById(favouriteMovieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null)
                    view.onSuccessFavouriteMovie(listOf(it))
            }, {
                view.onErrorFavouriteMovie()
                Log.i("muri", "errorGetMovieById: ${it.message}")
            }))
    }

    fun insertFavouriteMovie(favouriteMovies: List<FavouriteMovie>) {
        compositeDisposable.add(database.moviesDao().insertFavourite(favouriteMovies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("muri", "main: Inserted")
            }, {
                Log.i("muri", "dbpr: ${it.message}")
            }))
    }

    fun deleteFavouriteMovie(favouriteMovie: FavouriteMovie) {
        compositeDisposable.add(database.moviesDao().deleteFavourite(favouriteMovie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("muri", "main: Deleted")
            }) {
                Log.i("muri", "dbpr: ${it.message}")
            })
    }

    fun deleteAllFavouriteMovies() {
        compositeDisposable.add(database.moviesDao().deleteAllFavourite()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.i("muri", "dbpr: ${it.message}")
            }))
    }

    fun deleteAllFavouriteMoviesRX() = database.moviesDao().deleteAllFavourite()
}