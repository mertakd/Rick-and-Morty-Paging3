package com.sisifos.rickyandmortypaging3.ui.episode.detail.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.FragmentEpisodeDetailBottomSheetBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListItemSquareBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.squareup.picasso.Picasso

class EpisodeDetailViewHolder(
    private val binding: ModelCharacterListItemSquareBinding
): ViewHolder(binding.root){



    fun bind(character: Character){
        with(binding){
            characterNameTextView.text = character.name
            Picasso.get().load(character.image).into(characterImageView)
        }

    }
}