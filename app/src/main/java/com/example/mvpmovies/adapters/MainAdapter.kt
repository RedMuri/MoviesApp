package com.example.mvpmovies.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpmovies.R
import com.example.mvpmovies.pojo.Movie
import com.example.mvpmovies.presenters.ImagePresenter
import com.example.mvpmovies.view.FavouriteView

class MainAdapter(private var entities: ArrayList<Movie>,private var view: FavouriteView): RecyclerView.Adapter<MainAdapter.MovieViewHolder>() {
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageViewMoviePost: ImageView = itemView.findViewById(R.id.imageViewMoviePost)
        init {
            itemView.setOnClickListener { onMovieClickListener?.onMovieClick(adapterPosition) }
        }
    }
    interface OnMovieClickListener{
        fun onMovieClick(position: Int)
    }
    interface OnReachEndListener{
        fun onReachEnd()
    }
    private var onMovieClickListener: OnMovieClickListener? = null
    private var onReachEndListener: OnReachEndListener? = null
    fun setOnMovieClickListener(onMovieClickListener: OnMovieClickListener){
        this.onMovieClickListener = onMovieClickListener
    }
    fun setOnReachEndListener(onReachEndListener: OnReachEndListener){
        this.onReachEndListener = onReachEndListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view,parent,false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = entities[position]
        val presenter = ImagePresenter(view)
        presenter.loadSmallImage(movie.posterPath,holder.imageViewMoviePost)
        if (position>0 && entities.size - position==3)
            onReachEndListener?.onReachEnd()
    }

    override fun getItemCount() = entities.size

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movies: ArrayList<Movie>){
        this.entities = movies
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addMovies(movies: ArrayList<Movie>){
        this.entities.addAll(movies)
        notifyDataSetChanged()
    }
    fun getMovieByPosition(position: Int) = entities[position]
}