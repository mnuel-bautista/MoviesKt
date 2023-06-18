package com.manuel.movieapp.watchlist

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.manuel.movieapp.BuildConfig
import com.manuel.movieapp.common.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume

class WatchlistRepository @Inject constructor(
    private val client: OkHttpClient,
){

    private val url = "https://api.themoviedb.org/3/account/${BuildConfig.ACCOUNT_ID}/watchlist"

    private val moviesWatchlistUrl = "https://api.themoviedb.org/3/account/${BuildConfig.ACCOUNT_ID}/watchlist/movies"

    suspend fun getWatchlist(): List<Movie> {
        val movies = withContext(Dispatchers.IO) {

            val request = Request.Builder()
                .url(moviesWatchlistUrl)
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

    suspend fun addMovieToWatchlist(movieId: Int): AddToWatchListResponse {
        val body = """
            {
                "media_type": "movie",
                "media_id": $movieId,
                "watchlist": true
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .post(body.toRequestBody())
            .addHeader("content-type", "application/json;charset=utf-8")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()

        return suspendCancellableCoroutine { cont ->
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(AddToWatchListResponse.Error)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(AddToWatchListResponse.Success)
                }
            })

            cont.invokeOnCancellation {
                client.newCall(request).cancel()
            }
        }
    }

    suspend fun removeMovieFromWatchlist(movieId: Int): RemoveFromWatchListResponse {
        val body = """
            {
                "media_type": "movie",
                "media_id": $movieId,
                "watchlist": false
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .post(body.toRequestBody())
            .addHeader("content-type", "application/json;charset=utf-8")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()

        return suspendCancellableCoroutine { cont ->
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(RemoveFromWatchListResponse.Error)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(RemoveFromWatchListResponse.Success)
                }
            })

            cont.invokeOnCancellation {
                client.newCall(request).cancel()
            }
        }
    }

}