package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter


import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.databinding.AdapterCharacterItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.squareup.picasso.Picasso



class CharactersViewHolder(
    private val binding: AdapterCharacterItemBinding,
    private val onCharacterItemClickListener: ((Int) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {


    init {
        binding.root.setOnClickListener {
            onCharacterItemClickListener?.invoke(adapterPosition)
        }
    }

    fun bind(character: Character) {
        with(binding) {
            characterNameTextView.text = character.name
            Picasso.get().load(character.image).into(characterImageView)
        }
    }


}


