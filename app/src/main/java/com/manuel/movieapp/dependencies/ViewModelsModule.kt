package com.manuel.movieapp.dependencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manuel.movieapp.discover.DiscoverVM
import com.manuel.movieapp.movie.MovieDetailVM
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelsModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverVM::class)
    internal abstract fun discoverVM(viewModel: DiscoverVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailVM::class)
    internal abstract fun movieDetailVM(viewModel: MovieDetailVM): ViewModel

}


@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

