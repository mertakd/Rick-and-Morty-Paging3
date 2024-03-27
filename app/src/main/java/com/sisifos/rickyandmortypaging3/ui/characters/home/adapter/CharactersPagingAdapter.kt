package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.AdapterCharacterItemBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelCharacterListTitleBinding
import com.sisifos.rickyandmortypaging3.ui.characters.home.UiModel



class CharactersPagingAdapter : PagingDataAdapter<UiModel, ViewHolder>(REPO_COMPARATOR) {




    private var onCharacterItemClickListener: ((Int) -> Unit)? = null



    fun setOnCharacterItemClickListener(onCharacterItemClickListener: ((Int) -> Unit)?) {
        this.onCharacterItemClickListener = onCharacterItemClickListener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CHARACTERS_LIST_ITEM_VIEW_TYPE -> {
                CharactersViewHolder(
                    AdapterCharacterItemBinding.inflate(inflater, parent, false),
                    onCharacterItemClickListener
                )
            }

            CHARACTERS_HEADER_ITEM_VIEW_TYPE -> {
                CharactersSeparatorsViewHolder(
                    ModelCharacterListTitleBinding.inflate(inflater, parent, false)
                )
            }

            else -> throw Exception("Can not be constructed view holder with type:$viewType")
        }
    }



    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.CharacterItem -> CHARACTERS_LIST_ITEM_VIEW_TYPE
            is UiModel.SeparatorItem -> CHARACTERS_HEADER_ITEM_VIEW_TYPE
            else -> CHARACTERS_INVALID_VIEW_TYPE
        }
    }






    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val charactersUiModel = getItem(position)
        charactersUiModel?.let {
            when (charactersUiModel) {
                is UiModel.CharacterItem -> (holder as CharactersViewHolder).bind(charactersUiModel.character)
                is UiModel.SeparatorItem -> (holder as CharactersSeparatorsViewHolder).bind(charactersUiModel.text)

            }
        }
    }



    fun getItemAtPosition(position: Int): UiModel? {
        return getItem(position)
    }







    companion object {

        const val CHARACTERS_LIST_ITEM_VIEW_TYPE = 0
        const val CHARACTERS_HEADER_ITEM_VIEW_TYPE = 1
        const val CHARACTERS_INVALID_VIEW_TYPE = -1




        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                when {
                    oldItem is UiModel.CharacterItem && newItem is UiModel.CharacterItem -> oldItem.character.id == newItem.character.id
                    oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem -> oldItem.text == newItem.text
                    else -> false
                }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }




    }






}