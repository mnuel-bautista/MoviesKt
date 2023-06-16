package com.manuel.movieapp.discover.filter.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.manuel.movieapp.databinding.LanguageFilterBottomSheetLayoutBinding

class LanguageFilterBottomSheet(
    private val onLanguageSelected: (String) -> Unit,
): BottomSheetDialogFragment() {

    private var _binding: LanguageFilterBottomSheetLayoutBinding? = null

    private lateinit var adapter: LanguageAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedLanguage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LanguageFilterBottomSheetLayoutBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.languageList.layoutManager = LinearLayoutManager(requireContext())
        adapter = LanguageAdapter(
            onLanguageSelected = {position ->
                if(position != null) {
                    selectedLanguage = adapter.currentList[position].iso
                }
            }
        )

        binding.languageList.adapter = adapter

        binding.filterButton.setOnClickListener {
            selectedLanguage?.let {
                onLanguageSelected(it)
            }
            dismiss()
        }
    }

    fun submitList(languages: List<Language>) {
        adapter.submitList(languages)
    }

}