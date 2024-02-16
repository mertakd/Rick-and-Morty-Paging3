package com.sisifos.rickyandmortypaging3.ui.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListItemBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.inflateAdapterItem


class CharacterSearchAdapter : PagingDataAdapter<Character, CharacterSearchViewHolder>(REPO_COMPARATOR){


    private var onCharacterSearchItemClickListener: ((Int) -> Unit)? = null



    fun setOnCharacterSearchClickListener(onCharacterSearchClickListener: ((Int) -> Unit)?) {
        this.onCharacterSearchItemClickListener = onCharacterSearchClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterSearchViewHolder {
        val binding = parent.inflateAdapterItem(ModelCharacterListItemBinding::inflate)
        return CharacterSearchViewHolder(binding) { position ->
            onCharacterSearchItemClickListener?.invoke(position)
        }
    }

    override fun onBindViewHolder(holder: CharacterSearchViewHolder, position: Int) {
        val character = getItem(position)
        character?.let {
            holder.bind(it)
        }
    }


    fun getItemAtPosition(position: Int): Character? {
        return getItem(position)
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