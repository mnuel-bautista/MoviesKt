package com.manuel.movieapp.movie

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.elevation.SurfaceColors
import com.manuel.movieapp.MoviesApplication
import com.manuel.movieapp.R
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
                binding.collapsingToolbar.title = movie?.title
                binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
                binding.toolbar.setNavigationIconTint(resources.getColor(R.color.white))
                binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
                binding.toolbar.setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
                binding.movieOverview.text = movie?.overview
                binding.moviePoster.load("https://image.tmdb.org/t/p/w500${movie?.posterPath}") {
                    placeholder(R.drawable.poster_placeholder)
                    crossfade(true)
                    crossfade(400)
                }


                val margin = convertPixelsToDp(8, requireContext())

                movie?.genres?.forEach {
                    val textView = TextView(requireContext())
                    textView.text = it.name
                    textView.setBackgroundColor(SurfaceColors.SURFACE_2.getColor(requireContext()))
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { setMargins(margin, margin, margin, margin) }
                    textView.layoutParams = layoutParams

                    val paddingHorizontal = convertPixelsToDp(16, requireContext())
                    val paddingVertical = convertPixelsToDp(8, requireContext())
                    textView.setPadding(
                        paddingHorizontal,
                        paddingVertical,
                        paddingHorizontal,
                        paddingVertical
                    )

                    binding.genres.addView(textView)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.similar.collect { movies ->
                val similarMoviesAdapter = SimilarMovieAdapter(onMovieClick = {
                    findNavController().navigate(
                        MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(
                            it.id
                        )
                    )
                })
                binding.similarMoviesList.adapter = similarMoviesAdapter
                binding.similarMoviesList.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                similarMoviesAdapter.submitList(movies)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.accountState.collect { accountState ->
                if (accountState?.watchlist == true) {
                    binding.addToWatchlist.text = getString(R.string.remove_from_watchlist)
                    binding.addToWatchlist.icon = ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_playlist_add_check_24,
                        requireContext().theme
                    )
                } else {
                    binding.addToWatchlist.text = getString(R.string.add_to_watchlist)
                    binding.addToWatchlist.icon = ResourcesCompat.getDrawable(
                        resources, R.drawable.baseline_playlist_add_24,
                        requireContext().theme
                    )
                }
            }
        }

        binding.addToWatchlist.setOnClickListener {
            viewModel.toggleWatchlist()
        }
    }
}

private fun convertPixelsToDp(px: Int, context: Context): Int {
    return (px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
}