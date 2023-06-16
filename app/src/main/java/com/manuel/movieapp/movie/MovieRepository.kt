package com.manuel.movieapp.movie

import com.google.gson.Gson
import com.google.gson.JsonObject
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

    suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("$url/$movieId/similar")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()

            var movies: Array<Movie> = emptyArray()

            client.newCall(request).execute().use {
                if (it.isSuccessful) {
                    val gson = Gson()
                    val body = gson.fromJson(it.body!!.string(), JsonObject::class.java)

                    movies = gson.fromJson(body["results"], Array<Movie>::class.java)
                }
            }

            return@withContext movies.toList()
        }
    }
}