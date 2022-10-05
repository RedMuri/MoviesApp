package com.example.mvpmovies.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpmovies.R
import com.example.mvpmovies.adapters.MainAdapter
import com.example.mvpmovies.database.MovieDatabase
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie
import com.example.mvpmovies.pojo.Review
import com.example.mvpmovies.pojo.Video
import com.example.mvpmovies.presenters.MainPresenter
import com.example.mvpmovies.view.FavouriteView
import com.example.mvpmovies.view.MainView

class FavouriteActivity : AppCompatActivity(), MainView, FavouriteView {
    private lateinit var favouriteRecyclerView: RecyclerView
    private lateinit var favouriteRecyclerViewAdapter: MainAdapter
    private lateinit var mainPresenter: MainPresenter
    private lateinit var movieDatabase: MovieDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        favouriteRecyclerView = findViewById(R.id.favouriteRecyclerView)
        favouriteRecyclerViewAdapter = MainAdapter(arrayListOf(), this)
        favouriteRecyclerView.adapter = favouriteRecyclerViewAdapter
        favouriteRecyclerView.layoutManager =
            GridLayoutManager(this, spanCount(), GridLayoutManager.VERTICAL, false)
        movieDatabase = MovieDatabase.getInstance(this)
        mainPresenter = MainPresenter(this, movieDatabase)
        mainPresenter.getAllFavouriteMovies().observe(this) { list ->
            favouriteRecyclerViewAdapter.setMovies(list.map { Movie(it) } as ArrayList<Movie>)
        }
        favouriteRecyclerViewAdapter.setOnMovieClickListener(object :
            MainAdapter.OnMovieClickListener {
            override fun onMovieClick(position: Int) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java).apply {
                    val id = favouriteRecyclerViewAdapter.getMovieByPosition(position).id
                    Log.i("muri","$id")
                    putExtra("id", id)
                }
                startActivity(intent)
            }
        })
    }

    private fun spanCount(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels / resources.displayMetrics.density
        return if (width / 185 > 2) width.toInt() / 185 else 2
    }

    override fun onError(throwable: Throwable) {
        Log.i("muri", "in fav onError: $throwable")
    }

    override fun onSuccessMovie(movies: List<Movie>) {
        Log.i("muri", "in fav onSuccessMovie: $movies")
    }

    override fun onSuccessFavouriteMovie(favouriteMovies: List<FavouriteMovie>) {
        Log.i("muri", "in fav onSuccessFavouriteMovie: $favouriteMovies")
    }

    override fun onErrorFavouriteMovie() {
        Log.i("muri", "in fav onErrorFavouriteMovie: nothingness")
    }

    override fun onDestroy() {
        mainPresenter.disposeDisposable()
        super.onDestroy()
    }

    override fun onSuccessImage() {
    }

    override fun onSuccessVideos(videos: List<Video>) {
    }

    override fun onErrorVideos(message: String?) {
    }

    override fun onSuccessReviews(reviews: List<Review>) {
    }

    override fun onErrorReviews(message: String?) {

    }
}