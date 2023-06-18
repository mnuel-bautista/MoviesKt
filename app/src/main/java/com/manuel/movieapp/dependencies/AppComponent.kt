package com.manuel.movieapp.dependencies

import com.manuel.movieapp.discover.DiscoverFragment
import com.manuel.movieapp.movie.MovieDetailFragment
import com.manuel.movieapp.watchlist.WatchlistFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelsModule::class])
interface AppComponent {
    fun inject(fragment: DiscoverFragment)
    fun inject(fragment: MovieDetailFragment)

    fun inject(fragment: WatchlistFragment)
}