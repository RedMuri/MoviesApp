package com.example.mvpmovies.presenters

import android.util.Log
import android.widget.ImageView
import com.example.mvpmovies.R
import com.example.mvpmovies.pojo.FavouriteMovie
import com.example.mvpmovies.view.FavouriteView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class ImagePresenter(val view: FavouriteView) {
    private val compositeDisposable = CompositeDisposable()
    fun loadSmallImage(path: String, target: ImageView) {
        val url = "https://image.tmdb.org/t/p/w185$path"
        Picasso.get().load(url).placeholder(R.drawable.ic_launcher_background)
            .into(target, object : Callback {
                override fun onSuccess() {
                    view.onSuccessImage()
                }
                override fun onError(e: Exception?) {
                }
            })
    }

    fun loadBigImage(path: String, target: ImageView) {
        val url = "https://image.tmdb.org/t/p/w780$path"
        Picasso.get().load(url).placeholder(R.drawable.ic_launcher_background)
            .into(target, object : Callback {
                override fun onSuccess() {
                    view.onSuccessImage()
                }
                override fun onError(e: Exception?) {
                    loadSmallImage(path, target)
                    Log.i("muri", "HE loaded: $e")
                }
            })
    }
}