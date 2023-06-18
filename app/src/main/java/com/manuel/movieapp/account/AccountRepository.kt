package com.manuel.movieapp.account

import com.google.gson.Gson
import com.manuel.movieapp.BuildConfig
import com.manuel.movieapp.watchlist.AddToWatchListResponse
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

class AccountRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {

    private val url = "https://api.themoviedb.org/3/movie/{movie_id}/account_states"

    suspend fun getAccountState(movieId: Int): AccountStateResponse {
        val url = url.replace("{movie_id}", movieId.toString())

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
            .build()

        return suspendCancellableCoroutine { cont ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    cont.resume(AccountStateResponse.Error)
                }

                override fun onResponse(call: Call, response: Response) {

                    val gson = Gson()
                    val accountState =
                        gson.fromJson(response.body?.string(), AccountState::class.java)

                    cont.resume(AccountStateResponse.Success(accountState))
                }
            })

            cont.invokeOnCancellation {
                okHttpClient.newCall(request).cancel()
            }
        }
    }

}