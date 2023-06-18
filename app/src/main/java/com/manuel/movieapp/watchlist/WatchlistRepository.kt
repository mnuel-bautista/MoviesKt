package com.manuel.movieapp.watchlist

import com.manuel.movieapp.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
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
    private val okHttpClient: OkHttpClient,
){

    private val url = "https://api.themoviedb.org/3/account/${BuildConfig.ACCOUNT_ID}/watchlist"

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
            okHttpClient.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(AddToWatchListResponse.Error)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(AddToWatchListResponse.Success)
                }
            })

            cont.invokeOnCancellation {
                okHttpClient.newCall(request).cancel()
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
            okHttpClient.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(RemoveFromWatchListResponse.Error)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(RemoveFromWatchListResponse.Success)
                }
            })

            cont.invokeOnCancellation {
                okHttpClient.newCall(request).cancel()
            }
        }
    }

}