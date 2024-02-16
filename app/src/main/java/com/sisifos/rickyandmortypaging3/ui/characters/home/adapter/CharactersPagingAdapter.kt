package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter


import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.AdapterCharacterItemBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListTitleBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListItemBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListTitleBinding
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.inflateAdapterItem
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.EpisodesPagingAdapter
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.EpisodesViewHolder
import com.sisifos.rickyandmortypaging3.ui.episode.adapter.SeparatorViewHolder


class CharactersPagingAdapter : PagingDataAdapter<CharactersUiModel, ViewHolder>(REPO_COMPARATOR) {




    private var onCharacterItemClickListener: ((Int) -> Unit)? = null



    fun setOnCharacterItemClickListener(onCharacterItemClickListener: ((Int) -> Unit)?) {
        this.onCharacterItemClickListener = onCharacterItemClickListener
    }



    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = parent.inflateAdapterItem(AdapterCharacterItemBinding::inflate)
        return CharactersViewHolder(binding) { position ->
            onCharacterItemClickListener?.invoke(position)
        }
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CHARACTERS_LIST_ITEM_VIEW_TYPE -> {
                CharactersViewHolder(
                    parent.inflateAdapterItem(AdapterCharacterItemBinding::inflate),
                    onCharacterItemClickListener
                )
            }

            CHARACTERS_HEADER_ITEM_VIEW_TYPE -> {
                CharactersSeparatorsViewHolder(
                    parent.inflateAdapterItem(ModelCharacterListTitleBinding::inflate)
                )
            }

            else -> throw Exception("Can not be constructed view holder with type:$viewType")
        }
    }



    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CharactersUiModel.Item -> CHARACTERS_LIST_ITEM_VIEW_TYPE
            is CharactersUiModel.Header -> CHARACTERS_HEADER_ITEM_VIEW_TYPE
            else -> CHARACTERS_INVALID_VIEW_TYPE
        }
    }






    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val charactersUiModel = getItem(position)
        charactersUiModel?.let {
            when (charactersUiModel) {
                is CharactersUiModel.Item -> (holder as CharactersViewHolder).bind(charactersUiModel.character)
                is CharactersUiModel.Header -> (holder as CharactersSeparatorsViewHolder).bind(charactersUiModel.text)

            }
        }
    }

    /*override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = getItem(position)
        character?.let {
            holder.bind(it)
        }
    }*/

    fun getItemAtPosition(position: Int): CharactersUiModel? {
        return getItem(position)
    }




    /*companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem == newItem
        }


    }*/


    companion object {

        const val CHARACTERS_LIST_ITEM_VIEW_TYPE = 0
        const val CHARACTERS_HEADER_ITEM_VIEW_TYPE = 1
        const val CHARACTERS_INVALID_VIEW_TYPE = -1

        /* private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Episode>() {

             override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                 oldItem.id == newItem.id

             override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                 oldItem == newItem
         }*/




        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<CharactersUiModel>() {
            override fun areItemsTheSame(oldItem: CharactersUiModel, newItem: CharactersUiModel): Boolean =
                when {
                    oldItem is CharactersUiModel.Item && newItem is CharactersUiModel.Item -> oldItem.character.id == newItem.character.id
                    oldItem is CharactersUiModel.Header && newItem is CharactersUiModel.Header -> oldItem.text == newItem.text
                    else -> false
                }

            override fun areContentsTheSame(oldItem: CharactersUiModel, newItem: CharactersUiModel): Boolean =
                oldItem == newItem
        }





    }






}