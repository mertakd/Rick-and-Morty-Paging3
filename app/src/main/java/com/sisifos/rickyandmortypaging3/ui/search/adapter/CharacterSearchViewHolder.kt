package com.sisifos.rickyandmortypaging3.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.squareup.picasso.Picasso

class CharacterSearchViewHolder(
    private val binding: ModelCharacterListItemBinding,
    private val onCharacterSearchItemClickListener: ((Int) -> Unit)?
):ViewHolder(binding.root){

    init {
        binding.root.setOnClickListener {
            onCharacterSearchItemClickListener?.invoke(adapterPosition)
        }
    }


    fun bind(character: Character) {
        with(binding) {
            characterNameTextView.text = character.name
            Picasso.get().load(character.image).into(characterImageView)
        }
    }


}