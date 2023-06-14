package com.manuel.movieapp

import android.app.Application
import com.manuel.movieapp.dependencies.AppComponent
import com.manuel.movieapp.dependencies.DaggerAppComponent

class MoviesApplication: Application() {

    val appComponent: AppComponent = DaggerAppComponent.create()

}