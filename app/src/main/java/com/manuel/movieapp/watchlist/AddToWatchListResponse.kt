package com.manuel.movieapp.watchlist

sealed class AddToWatchListResponse {
    object Success: AddToWatchListResponse()
    object Error: AddToWatchListResponse()
}

sealed class RemoveFromWatchListResponse {
    object Success: RemoveFromWatchListResponse()
    object Error: RemoveFromWatchListResponse()
}