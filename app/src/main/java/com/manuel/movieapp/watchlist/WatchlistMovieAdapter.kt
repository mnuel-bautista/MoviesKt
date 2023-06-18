package com.manuel.movieapp.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manuel.movieapp.R
import com.manuel.movieapp.common.Movie


class WatchlistMovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, WatchlistMovieAdapter.WatchlistMovieViewHolder>(diffUtil) {

    inner class WatchlistMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.movie_title)
                .text = movie.title

            itemView.findViewById<TextView>(R.id.movie_overview)
                .text = movie.overview
            val image = itemView.findViewById<ImageView>(R.id.movie_poster)
            image.load("https://image.tmdb.org/t/p/w500/${movie.posterPath}") {
                placeholder(R.drawable.poster_placeholder)
                error(R.drawable.poster_placeholder)
            }
            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistMovieViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.watch_list_movie_item, parent, false)
            .let { WatchlistMovieViewHolder(it) }
    }

    override fun onBindViewHolder(holder: WatchlistMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}