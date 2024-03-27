package com.sisifos.rickyandmortypaging3.ui.characters.detail.adapter


import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeCarouselItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Episode



class EpisodeCarouselViewHolder(

    private val binding: ModelEpisodeCarouselItemBinding,
    private val onEpisodeCarouselItemClickListener: ((Int) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {


    init {
        binding.root.setOnClickListener {
            onEpisodeCarouselItemClickListener?.invoke(adapterPosition)
        }
    }

    fun bind(episode: Episode) {
        with(binding) {
            episodeTextView.text = episode.getFormattedSeasonTruncated()
            episodeDetailsTextView.text = "${episode.name}\n${episode.airDate}"

        }
    }


}