package com.manuel.movieapp.movie

import com.manuel.movieapp.common.Movie

sealed class MovieResponse {

    class Success(val movie: Movie): MovieResponse()

    object MovieNotFound: MovieResponse()

}