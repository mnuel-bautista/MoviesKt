package com.manuel.movieapp.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuel.movieapp.common.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WatchlistVM @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
) : ViewModel() {

    private val _movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    init {
        viewModelScope.launch {
            _movies.value = watchlistRepository.getWatchlist()
        }
    }

}