package com.manuel.movieapp.movie

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


class SimilarMovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, SimilarMovieAdapter.SimilarMovieViewHolder>(diffUtil) {

    inner class SimilarMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.movie_title)
                .text = movie.title
            val image = itemView.findViewById<ImageView>(R.id.movie_poster)
            image.load("https://image.tmdb.org/t/p/w500/${movie.posterPath}") {
                placeholder(R.drawable.poster_placeholder)
                error(R.drawable.ic_launcher_background)
            }
            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.similar_movie_item_layout, parent, false)
            .let { SimilarMovieViewHolder(it) }
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
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