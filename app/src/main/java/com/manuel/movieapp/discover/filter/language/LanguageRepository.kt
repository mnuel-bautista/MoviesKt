package com.manuel.movieapp.discover.filter.language

import com.google.gson.Gson
import com.manuel.movieapp.BuildConfig
import com.manuel.movieapp.common.Movie
import com.manuel.movieapp.movie.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    private val client: OkHttpClient
) {

    private val url = "https://api.themoviedb.org/3/configuration/languages"

    suspend fun getLanguages(): List<Language> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()

            client.newCall(request).execute().use {
                var languages: List<Language> = emptyList()
                if (it.code == 404) {
                    throw Exception("Error")
                }

                if (it.isSuccessful) {
                    val gson = Gson()
                    languages = gson.fromJson(it.body!!.string(), Array<Language>::class.java)
                        .toList()
                }

                languages
            }
        }
    }
}