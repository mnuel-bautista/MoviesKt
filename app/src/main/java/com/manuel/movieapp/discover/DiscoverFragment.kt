package com.manuel.movieapp.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.manuel.movieapp.MoviesApplication
import com.manuel.movieapp.R
import com.manuel.movieapp.databinding.FragmentDiscoverBinding
import com.manuel.movieapp.discover.filter.language.LanguageFilterBottomSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DiscoverFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DiscoverVM

    private var _binding: FragmentDiscoverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun showLanguageFilterBottomSheet() {
        var job: Job? = null
        val bottomSheet = LanguageFilterBottomSheet(
            onLanguageSelected = { language ->
                Toast.makeText(requireContext(), language, Toast.LENGTH_SHORT).show()
                viewModel.setLanguage(language)
                job?.cancel()
            }
        )

        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLanguages()
            delay(200)
            viewModel.languages.collect {
                bottomSheet.submitList(it)
            }
        }
        bottomSheet.show(parentFragmentManager, "language_filter")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (requireActivity().application as MoviesApplication).appComponent
        appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[DiscoverVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAdapter(onMovieClick = { movie ->
            findNavController().navigate(
                DiscoverFragmentDirections.actionDiscoveryFragmentToMovieDetailFragment(movie.id)
            )
        })

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.moviesRecyclerView.layoutManager = layoutManager
        binding.moviesRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collect {
                adapter.submitList(it)
            }
        }

        binding.languageFilterChip.setOnClickListener {
            showLanguageFilterBottomSheet()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.languageFilter.collect { language ->
                val chip = binding.languageFilterChip

                chip.text = language ?: resources.getString(R.string.language)
                chip.isCheckedIconVisible = true
                chip.isChecked = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}