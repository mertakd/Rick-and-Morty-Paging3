package com.sisifos.rickyandmortypaging3.ui.characters.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeCarouselItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Episode



class EpisodeCarouselAdapter : PagingDataAdapter<Episode, EpisodeCarouselViewHolder>(REPO_COMPARATOR){


    private var onEpisodeCarouselItemClickListener: ((Int) -> Unit)? = null



    fun setOnEpisodeItemClickListener(onCharacterItemClickListener: ((Int) -> Unit)?) {
        this.onEpisodeCarouselItemClickListener = onCharacterItemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeCarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ModelEpisodeCarouselItemBinding.inflate(inflater, parent, false)
        //val binding = parent.inflateAdapterItem(ModelEpisodeCarouselItemBinding::inflate)
        return EpisodeCarouselViewHolder(binding) { position ->
            onEpisodeCarouselItemClickListener?.invoke(position)
        }
    }

    override fun onBindViewHolder(holder: EpisodeCarouselViewHolder, position: Int) {
        val episode = getItem(position)
        episode?.let {
            holder.bind(it)
        }
    }


    fun getItemAtPosition(position: Int): Episode? {
        return getItem(position)
    }


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem == newItem
        }


    }




}