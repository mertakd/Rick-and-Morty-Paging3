package com.sisifos.rickyandmortypaging3.ui.episode.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListItemSquareBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character


class EpisodeDetailAdapter : PagingDataAdapter<Character, EpisodeDetailViewHolder>(REPO_COMPARATOR){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ModelCharacterListItemSquareBinding.inflate(inflater,parent,false)
        return EpisodeDetailViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EpisodeDetailViewHolder, position: Int) {
        val episodeBottomSheet = getItem(position)
        episodeBottomSheet?.let {
            holder.bind(it)
        }
    }




    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem
        }


    }






}