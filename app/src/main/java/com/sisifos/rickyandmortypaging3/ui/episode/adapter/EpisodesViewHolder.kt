package com.sisifos.rickyandmortypaging3.ui.episode.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Episode

class EpisodesViewHolder(
    private val binding: ModelEpisodeListItemBinding,
    private val onEpisodeItemClickListener: ((Int) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onEpisodeItemClickListener?.invoke(bindingAdapterPosition)
        }
    }


    fun bind(episode: Episode) {
        with(binding) {
            episodeNameTextView.text = episode.name
            episodeAirDateTextView.text = episode.airDate
            episodeNumberTextView.text = episode.getFormattedSeasonTruncated()
        }
    }
}