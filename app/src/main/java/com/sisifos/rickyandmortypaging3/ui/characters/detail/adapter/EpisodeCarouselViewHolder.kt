package com.sisifos.rickyandmortypaging3.ui.characters.detail.adapter


import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeCarouselItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.ui.base.BaseViewHolder


class EpisodeCarouselViewHolder(

    private val binding: ModelEpisodeCarouselItemBinding,
    private val onEpisodeCarouselItemClickListener: ((Int) -> Unit)?
): BaseViewHolder<Episode>(binding.root) {


    init {
        binding.root.setOnClickListener {
            onEpisodeCarouselItemClickListener?.invoke(adapterPosition)
        }
    }

    override fun bind(episode: Episode) {
        with(binding) {
            episodeTextView.text = episode.getFormattedSeasonTruncated()
            episodeDetailsTextView.text = "${episode.name}\n${episode.airDate}"

        }
    }


}