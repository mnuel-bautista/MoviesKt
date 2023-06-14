package com.manuel.movieapp.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuel.movieapp.common.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailVM @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _movie: MutableStateFlow<Movie?> = MutableStateFlow(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            when (val result = movieRepository.getMovie(movieId)) {
                MovieResponse.MovieNotFound -> {
                    // TODO
                }

                is MovieResponse.Success -> {
                    _movie.value = result.movie
                }
            }
        }

    }
}