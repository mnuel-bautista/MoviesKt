package com.manuel.movieapp.dependencies

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}