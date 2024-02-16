package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListTitleBinding

class CharactersSeparatorsViewHolder(private val binding: ModelCharacterListTitleBinding): RecyclerView.ViewHolder(binding.root) {


    private val description: TextView = binding.characterSeparatorDescription

    fun bind(separatorText: String) {
        description.text = separatorText
    }
}