package com.manuel.movieapp.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.manuel.movieapp.MoviesApplication
import com.manuel.movieapp.databinding.FragmentWatchlistBinding
import com.manuel.movieapp.discover.DiscoverFragmentDirections
import com.manuel.movieapp.discover.MovieAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject


class WatchlistFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: WatchlistVM

    private var _binding: FragmentWatchlistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (requireActivity().application as MoviesApplication).appComponent
        appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[WatchlistVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WatchlistMovieAdapter(onMovieClick = { movie ->
            findNavController().navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToMovieDetailFragment(movie.id)
            )
        })

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.movieList.layoutManager = layoutManager
        binding.movieList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collect {
                adapter.submitList(it)
            }
        }
    }

}