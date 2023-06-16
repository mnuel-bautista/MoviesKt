package com.manuel.movieapp.discover.filter.language

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.elevation.SurfaceColors
import com.manuel.movieapp.R

class LanguageAdapter(
    private val onLanguageSelected: (Int?) -> Unit
) : ListAdapter<Language, LanguageAdapter.LanguageViewHolder>(diffUtil) {

    private var selectedLanguagePosition: Int? = null

    inner class LanguageViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(language: Language) {

            if(selectedLanguagePosition == adapterPosition) {
                itemView.setBackgroundColor(SurfaceColors.SURFACE_3.getColor(itemView.context))
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            val languageTv = itemView.findViewById<TextView>(R.id.language)
            languageTv.text = language.englishName
            itemView.setOnClickListener {
                onLanguageSelected(adapterPosition)

                if(selectedLanguagePosition != null) {
                    notifyItemChanged(selectedLanguagePosition!!)
                }

                selectedLanguagePosition = adapterPosition
                notifyItemChanged(selectedLanguagePosition!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.language_item_layout, parent, false)
            .let { LanguageViewHolder(it) }
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<Language>() {
    override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem.iso == newItem.iso
    }

    override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
        return oldItem == newItem
    }
}