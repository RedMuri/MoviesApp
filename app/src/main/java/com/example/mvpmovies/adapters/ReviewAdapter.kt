package com.example.mvpmovies.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpmovies.R
import com.example.mvpmovies.pojo.Review
import com.example.mvpmovies.pojo.Video

class ReviewAdapter(private var reviews: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {
    class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewAuthor: TextView  = itemView.findViewById(R.id.textViewReviewAuthor)
        val reviewData: TextView = itemView.findViewById(R.id.textViewReviewData)
        val reviewContent : TextView = itemView.findViewById(R.id.textViewReviewContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val review = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewHolder(review)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        val review = reviews[position]
        holder.reviewAuthor.text = review.author
        holder.reviewData.text = review.data
        holder.reviewContent.text = review.content
    }

    override fun getItemCount() = reviews.size

    @SuppressLint("NotifyDataSetChanged")
    fun setReviews(reviews: ArrayList<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

}