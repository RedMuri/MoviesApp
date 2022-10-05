package com.example.mvpmovies.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpmovies.R
import com.example.mvpmovies.adapters.ReviewAdapter
import com.example.mvpmovies.adapters.VideoAdapter
import com.example.mvpmovies.database.MovieDatabase
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.pojo.Movie
import com.example.mvpmovies.pojo.Review
import com.example.mvpmovies.pojo.Video
import com.example.mvpmovies.presenters.ImagePresenter
import com.example.mvpmovies.presenters.MainPresenter
import com.example.mvpmovies.view.FavouriteView
import com.example.mvpmovies.view.MainView
import io.reactivex.disposables.CompositeDisposable

class DetailActivity : AppCompatActivity(), MainView, FavouriteView {
    private var id = -1
    private var isFavourite = false
    private var favouriteMovie: FavouriteMovie? = null
    private lateinit var mainPresenter: MainPresenter
    private lateinit var imagePresenter: ImagePresenter
    private lateinit var database: MovieDatabase
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var movie: Movie

    private lateinit var detailImageViewPost: ImageView
    private lateinit var detailTextViewTitle: TextView
    private lateinit var detailTextViewReleaseDate: TextView
    private lateinit var detailTextViewDescription: TextView
    private lateinit var detailTextViewPopularity: TextView
    private lateinit var detailTextViewVoteCount: TextView
    private lateinit var detailTextViewVoteAverage: TextView
    private lateinit var detailButtonFavorite: ImageView

    private lateinit var videosRecyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var videosProgress: ProgressBar
    private lateinit var reviewsProgress: ProgressBar

    private lateinit var errorVideosNotFound: TextView
    private lateinit var errorReviewsNotFound: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        id = intent.getIntExtra("id", -1)
        database = MovieDatabase.getInstance(this)
        mainPresenter = MainPresenter(this, database)
        imagePresenter = ImagePresenter(this)

        detailImageViewPost = findViewById(R.id.detailImageViewPost)
        detailTextViewTitle = findViewById(R.id.detailTextViewTitle)
        detailTextViewReleaseDate = findViewById(R.id.detailTextViewReleaseDate)
        detailTextViewDescription = findViewById(R.id.detailTextViewDescription)
        detailTextViewPopularity = findViewById(R.id.detailTextViewPopularity)
        detailTextViewVoteCount = findViewById(R.id.detailTextViewVoteCount)
        detailTextViewVoteAverage = findViewById(R.id.detailTextViewVoteAverage)
        detailButtonFavorite = findViewById(R.id.detailButtonFavorite)
        compositeDisposable = CompositeDisposable()
        videosRecyclerView = findViewById(R.id.videosRecyclerView)
        videosRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        videoAdapter = VideoAdapter(arrayListOf())
        videosRecyclerView.adapter = videoAdapter

        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)
        reviewsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviewAdapter = ReviewAdapter(arrayListOf())
        reviewsRecyclerView.adapter = reviewAdapter

        videosProgress = findViewById(R.id.videosProgress)
        reviewsProgress = findViewById(R.id.reviewsProgress)

        errorVideosNotFound = findViewById(R.id.errorVideosNotFound)
        errorReviewsNotFound = findViewById(R.id.errorReviewsNotFound)

        detailButtonFavorite.setOnClickListener { onFavoriteClick() }
        detailButtonFavorite.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val view = v as ImageView
                    view.drawable.colorFilter =
                        PorterDuffColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                    view.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    val view = v as ImageView
                    view.drawable.clearColorFilter()
                    view.invalidate()
                }
                MotionEvent.ACTION_CANCEL -> {
                    val view = v as ImageView
                    view.drawable.clearColorFilter()
                    view.invalidate()
                }
            }
            return@setOnTouchListener false
        }
        videoAdapter.setOnVideoClickListener(object : VideoAdapter.OnVideoClickListener {
            override fun onVideoClick(position: Int, key: String) {
                val uri = "https://www.youtube.com/watch?v=$key"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
        })

        mainPresenter.getFavouriteMovieById(id)
        mainPresenter.getMovieById(id)
        mainPresenter.loadVideos(id)
        mainPresenter.loadReviews(id)
    }

    private fun onFavoriteClick() {
        if (isFavourite) {
            isFavourite = false
            if (favouriteMovie != null) {
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
                mainPresenter.deleteFavouriteMovie(favouriteMovie!!)
            }
            detailButtonFavorite.setImageResource(android.R.drawable.btn_star_big_off)
        } else {
            isFavourite = true
            val mov = FavouriteMovie(movie)
            favouriteMovie = mov
            mainPresenter.insertFavouriteMovie(listOf(mov))
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
            Log.i("muri", "movie: ${movie.id}, favourite: ${mov.id}")
            detailButtonFavorite.setImageResource(android.R.drawable.btn_star_big_on)
        }
    }

    override fun onDestroy() {
        mainPresenter.disposeDisposable()
        super.onDestroy()
    }

    override fun onError(throwable: Throwable) {
        if (favouriteMovie != null) {
            onSuccessMovie(listOf(Movie(favouriteMovie!!)))
        }
        Log.i("muri", throwable.message.toString())
    }

    override fun onSuccessMovie(movies: List<Movie>) {
        movie = movies[0]
        imagePresenter.loadBigImage(movie.posterPath, detailImageViewPost)
        detailTextViewTitle.text = movie.title
        detailTextViewReleaseDate.text = movie.releaseDate
        detailTextViewDescription.text = movie.overview
        detailTextViewPopularity.text = movie.popularity.toString()
        detailTextViewVoteCount.text = movie.voteCount.toString()
        detailTextViewVoteAverage.text = movie.voteAverage.toString()
    }

    override fun onSuccessFavouriteMovie(favouriteMovies: List<FavouriteMovie>) {
        favouriteMovie = favouriteMovies[0]
        onSuccessMovie(listOf(Movie(favouriteMovie!!)))
        isFavourite = true
        detailButtonFavorite.setImageResource(android.R.drawable.btn_star_big_on)
    }

    override fun onErrorFavouriteMovie() {
        isFavourite = false
        detailButtonFavorite.setImageResource(android.R.drawable.btn_star_big_off)
    }

    override fun onSuccessImage() {
    }

    override fun onSuccessVideos(videos: List<Video>) {
        videosProgress.visibility = View.INVISIBLE
        videoAdapter.setVideos(videos as ArrayList<Video>)
    }

    override fun onErrorVideos(message: String?) {
        videosProgress.visibility = View.INVISIBLE
        errorVideosNotFound.visibility = View.VISIBLE
        errorVideosNotFound.text = when (message) {
            "Empty" -> getString(R.string.on_error_videos_empty)
            "timeout" -> getString(R.string.on_videos_error_timeout)
            else -> getString(R.string.on_videos_error_else)
        }
    }

    override fun onSuccessReviews(reviews: List<Review>) {
        reviewsProgress.visibility = View.INVISIBLE
        reviewAdapter.setReviews(reviews as ArrayList<Review>)
    }

    override fun onErrorReviews(message: String?) {
        reviewsProgress.visibility = View.INVISIBLE
        errorReviewsNotFound.visibility = View.VISIBLE
        errorReviewsNotFound.text = when (message) {
            "Empty" -> getString(R.string.on_error_reviews_empty)
            "timeout" -> getString(R.string.on_reviews_error_timeout)
            else -> getString(R.string.on_reviews_error_else)
        }
    }
}
