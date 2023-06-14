package com.manuel.movieapp.movie

import com.google.gson.Gson
import com.manuel.movieapp.BuildConfig
import com.manuel.movieapp.common.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val client: OkHttpClient
) {

    private val url = "https://api.themoviedb.org/3/movie"

    suspend fun getMovie(id: Int): MovieResponse {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$url/$id")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()

           client.newCall(request).execute().use {
               var response: MovieResponse = MovieResponse.MovieNotFound
                if (it.code == 404) {
                    response = MovieResponse.MovieNotFound
                }

                if (it.isSuccessful) {
                    val gson = Gson()
                    val movie = gson.fromJson(it.body!!.string(), Movie::class.java)
                    response = MovieResponse.Success(movie)
                }
               response
            }
        }
    }
}