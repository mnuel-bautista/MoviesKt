package com.manuel.movieapp.discover

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.manuel.movieapp.BuildConfig
import com.manuel.movieapp.common.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import javax.inject.Inject

class DiscoverRepository @Inject constructor(
    private val client: OkHttpClient
) {

    private val url = "https://api.themoviedb.org/3/discover/movie?page=1&sort_by=popularity.desc"

    /**
     * @param language Filter movies by this language. If not specified, all languages will be returned.
     * */
    suspend fun getMovies(
        language: String? = null
    ): List<Movie> {
        val movies = withContext(Dispatchers.IO) {
            val url = if (language != null) "$url&language=$language" else url

            val request = okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val gson = Gson()
                    val body = gson.fromJson(response.body!!.string(), JsonObject::class.java)

                    return@withContext gson.fromJson(body["results"], Array<Movie>::class.java)
                } else {
                    throw Exception("Error getting movies")
                }
            }
        }
        return movies.toList()
    }

}