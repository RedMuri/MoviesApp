package com.example.mvpmovies.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpmovies.R
import com.example.mvpmovies.pojo.Video

class VideoAdapter(private var videos: ArrayList<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoHolder>() {
    inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitle: TextView = itemView.findViewById(R.id.textViewVideoTitle)

        init {
            itemView.setOnClickListener { onVideoClickListener?.onVideoClick(adapterPosition,videos[adapterPosition].key) }
        }
    }

    interface OnVideoClickListener {
        fun onVideoClick(position: Int,key: String)
    }

    private var onVideoClickListener: OnVideoClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val video = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return VideoHolder(video)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val video = videos[position]
        holder.videoTitle.text =
            if (video.name.length > 14) "${video.name.take(14)}..." else video.name
    }

    override fun getItemCount() = videos.size

    @SuppressLint("NotifyDataSetChanged")
    fun setVideos(videos: ArrayList<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }

    fun setOnVideoClickListener(onVideoClickListener: OnVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener
    }
}