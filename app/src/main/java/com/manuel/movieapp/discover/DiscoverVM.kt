package com.manuel.movieapp.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuel.movieapp.common.Movie
import com.manuel.movieapp.discover.filter.language.Language
import com.manuel.movieapp.discover.filter.language.LanguageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverVM @Inject constructor(
    private val repository: DiscoverRepository,
    private val languageRepository: LanguageRepository,
): ViewModel() {

    private val _movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _languages: MutableStateFlow<List<Language>> = MutableStateFlow(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    private val _languageFilter: MutableStateFlow<String?> = MutableStateFlow(null)
    val languageFilter: StateFlow<String?> = _languageFilter.asStateFlow()

    init {
        viewModelScope.launch {
            _movies.value = repository.getMovies()
        }
    }

    fun getLanguages() {
        viewModelScope.launch {
            _languages.value = languageRepository.getLanguages()
        }
    }

    fun setLanguage(language: String) {
        _languageFilter.value = language

        viewModelScope.launch {
            _movies.value = repository.getMovies(language)
        }
    }

}