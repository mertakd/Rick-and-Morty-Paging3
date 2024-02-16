package com.sisifos.rickyandmortypaging3.ui.episode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListItemBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListTitleBinding

class SeparatorViewHolder(private val binding: ModelEpisodeListTitleBinding) : RecyclerView.ViewHolder(binding.root) {

    private val description: TextView = binding.separatorDescription

    fun bind(separatorText: String) {
        description.text = separatorText
    }


}