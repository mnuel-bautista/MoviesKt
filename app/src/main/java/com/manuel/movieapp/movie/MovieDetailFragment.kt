package com.manuel.movieapp.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.manuel.movieapp.MoviesApplication
import com.manuel.movieapp.databinding.FragmentMovieDetailBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MovieDetailVM

    private var _binding: FragmentMovieDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (requireActivity().application as MoviesApplication).appComponent
        appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieDetailVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovie(MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                binding.movieTitle.text = movie?.title
                binding.movieOverview.text = movie?.overview
                binding.moviePoster.load("https://image.tmdb.org/t/p/w500${movie?.posterPath}") {
                    crossfade(true)
                    crossfade(1000)
                }
            }
        }
    }
}