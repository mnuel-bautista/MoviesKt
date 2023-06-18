package com.manuel.movieapp.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuel.movieapp.account.AccountRepository
import com.manuel.movieapp.common.Movie
import com.manuel.movieapp.account.AccountState
import com.manuel.movieapp.account.AccountStateResponse
import com.manuel.movieapp.watchlist.AddToWatchListResponse
import com.manuel.movieapp.watchlist.RemoveFromWatchListResponse
import com.manuel.movieapp.watchlist.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailVM @Inject constructor(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _movie: MutableStateFlow<Movie?> = MutableStateFlow(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    private val _similar: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val similar: StateFlow<List<Movie>> = _similar.asStateFlow()

    private val _accountState: MutableStateFlow<AccountState?> = MutableStateFlow(null)
    val accountState: StateFlow<AccountState?> = _accountState.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            when (val result = movieRepository.getMovie(movieId)) {
                MovieResponse.MovieNotFound -> {
                    // TODO
                }

                is MovieResponse.Success -> {
                    _movie.value = result.movie
                    getSimilarMovies(movieId)
                    getAccountState(movieId)
                }
            }
        }
    }

    private fun getAccountState(movieId: Int) {
        viewModelScope.launch {
            when (val result = accountRepository.getAccountState(movieId)) {
                is AccountStateResponse.Success -> {
                    _accountState.value = result.accountState
                }
                AccountStateResponse.Error -> {

                }
            }
        }
    }

    private suspend fun getSimilarMovies(movieId: Int) {
        _similar.value = movieRepository.getSimilarMovies(movieId)
    }

    fun toggleWatchlist() {
        val isOnWatchlist = accountState.value?.watchlist ?: false

        if (isOnWatchlist) {
            removeFromWatchlist()
        } else {
            addToWatchlist()
        }
    }

    private fun addToWatchlist() {
        viewModelScope.launch {
            val movieId = movie.value?.id ?: return@launch
            when (watchlistRepository.addMovieToWatchlist(movieId)) {
                AddToWatchListResponse.Success -> {
                    _accountState.value = accountState.value?.copy(watchlist = true)
                }

                AddToWatchListResponse.Error -> {

                }
            }
        }
    }

    private fun removeFromWatchlist() {
        viewModelScope.launch {
            val movieId = movie.value?.id ?: return@launch
            when (watchlistRepository.removeMovieFromWatchlist(movieId)) {
                RemoveFromWatchListResponse.Success -> {
                    _accountState.value = accountState.value?.copy(watchlist = false)
                }

                RemoveFromWatchListResponse.Error -> {

                }
            }
        }
    }
}