package com.manuel.movieapp.account

data class AccountState(
    val id: Int,
    val favorite: Boolean,
    val rated: Boolean,
    val watchlist: Boolean
)

data class Rated(
    val value: Double
)