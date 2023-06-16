package com.manuel.movieapp.common

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("released_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String,
    val overview: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    val adult: Boolean,
    val genres: List<Genre>,
)

data class Genre(
    val id: Int,
    val name: String
)