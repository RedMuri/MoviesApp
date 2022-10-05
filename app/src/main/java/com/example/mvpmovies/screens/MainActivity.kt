package com.example.mvpmovies.screens

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
import com.example.mvpmovies.utils.Utils
import com.example.mvpmovies.view.FavouriteView
import com.example.mvpmovies.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), MainView, FavouriteView {
    private lateinit var mainTextViewPopular: TextView
    private lateinit var mainSwitcher: SwitchCompat
    private lateinit var mainTextViewTopRated: TextView
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainPresenter: MainPresenter
    private lateinit var compositeDisposable: CompositeDisposable
    private var error = false
    private var page = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainTextViewPopular = findViewById(R.id.mainTextViewPopular)
        mainSwitcher = findViewById(R.id.mainSwitcher)
        mainTextViewTopRated = findViewById(R.id.mainTextViewTopRated)
        mainRecyclerView = findViewById(R.id.mainRecyclerView)
        mainAdapter = MainAdapter(arrayListOf(), this)
        mainPresenter = MainPresenter(this, MovieDatabase.getInstance(this))
        compositeDisposable = CompositeDisposable()

        mainRecyclerView.adapter = mainAdapter
        mainRecyclerView.layoutManager = GridLayoutManager(this, getSpanCount())
        loadFromDB()
        isLoading = true
        mainSwitcher.isChecked = true
        mainTextViewPopular.setOnClickListener {
            mainSwitcher.isChecked = true
        }
        mainTextViewTopRated.setOnClickListener {
            mainSwitcher.isChecked = false
        }
        mainSwitcher.setOnCheckedChangeListener { _, isChecked ->
            page = 1
            if (isChecked) {
                mainTextViewTopRated.setTextColor(Color.WHITE)
                mainTextViewPopular.setTextColor(Color.RED)
                mainPresenter.loadData(Utils.POPULAR, page)
                isLoading = true
            } else {
                mainTextViewTopRated.setTextColor(Color.RED)
                mainTextViewPopular.setTextColor(Color.WHITE)
                mainPresenter.loadData(Utils.TOP_RATED, page)
                isLoading = true
            }
        }
        mainSwitcher.isChecked = false
        mainAdapter.setOnMovieClickListener(object : MainAdapter.OnMovieClickListener {
            override fun onMovieClick(position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra("id",
                        mainAdapter.getMovieByPosition(position).id)
                }
                startActivity(intent)
            }
        })
        mainAdapter.setOnReachEndListener(object : MainAdapter.OnReachEndListener {
            override fun onReachEnd() {
                if (!isLoading) {
                    page++
                    if (mainSwitcher.isChecked) {
                        mainPresenter.loadData(Utils.POPULAR, page)
                        isLoading = true
                    } else {
                        mainPresenter.loadData(Utils.TOP_RATED, page)
                        isLoading = true
                    }
                    Log.i("muri", page.toString())
                }
            }
        })
    }


    private fun getSpanCount(): Int {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels / resources.displayMetrics.density
        return if (width / 185 > 2) width.toInt() / 185 else 2
    }

    override fun onDestroy() {
        mainPresenter.disposeDisposable()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun loadFromDB() {
        mainPresenter.getAllMovies().observe(this) { list ->
            Log.i("muri", "listSize: " + list.size.toString())
            if (page == 1)
                mainAdapter.setMovies(list as ArrayList<Movie>)
        }
        isLoading = false
    }

    override fun onError(throwable: Throwable) {
        isLoading = false
        error = true
        Log.i("muri", throwable.message.toString())
    }

    override fun onSuccessMovie(movies: List<Movie>) {
        isLoading = false
        if (movies.isNotEmpty()) {
            if (page == 1) {
                compositeDisposable.add(mainPresenter.deleteAllMoviesRX()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mainPresenter.insertMovie(movies)
                    }, {
                        Log.i("muri", "mainDelete: ${it.message}")
                    }))
            } else {
                mainPresenter.insertMovie(movies)
                mainAdapter.addMovies(movies as ArrayList<Movie>)
            }
        }
    }

    override fun onSuccessFavouriteMovie(favouriteMovies: List<FavouriteMovie>) {
        Log.i("muri", "Whattahell u doin in main activity??")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSuccessImage() {
    }

    override fun onErrorFavouriteMovie() {
        Log.i("muri", "Whattahell u doin in main activity??")
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
