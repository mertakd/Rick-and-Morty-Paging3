package com.sisifos.rickyandmortypaging3.ui.episode.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListItemBinding
import com.sisifos.rickyandmortypaging3.databinding.ModelEpisodeListTitleBinding
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel


class EpisodesPagingAdapter : PagingDataAdapter<EpisodesUiModel, ViewHolder>(REPO_COMPARATOR) {



    private var onEpisodeItemClickListener: ((Int) -> Unit)? = null


    fun setOnEpisodeItemClickListener(onEpisodeItemClickListener: ((Int) -> Unit)?) {
        this.onEpisodeItemClickListener = onEpisodeItemClickListener
    }



    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = parent.inflateAdapterItem(ModelEpisodeListItemBinding::inflate)
        return EpisodesViewHolder(binding) {position ->
            onEpisodeItemClickListener?.invoke(position)
        }
    }*/
    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.model_episode_list_item -> {
                val binding =
                    ModelEpisodeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EpisodesViewHolder(binding, onEpisodeItemClickListener)
            }
            R.layout.model_episode_list_title -> {
                val binding =
                    ModelEpisodeListTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SeparatorViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            LIST_ITEM_VIEW_TYPE -> {
                EpisodesViewHolder(
                    ModelEpisodeListItemBinding.inflate(inflater, parent, false),
                    onEpisodeItemClickListener
                )
            }

            HEADER_ITEM_VIEW_TYPE -> {
                SeparatorViewHolder(
                    ModelEpisodeListTitleBinding.inflate(inflater,parent,false)
                )
            }

            else -> throw Exception("Can not be constructed view holder with type:$viewType")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EpisodesUiModel.Item -> LIST_ITEM_VIEW_TYPE
            is EpisodesUiModel.Header -> HEADER_ITEM_VIEW_TYPE
            else -> INVALID_VIEW_TYPE
        }
    }


    /*override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episodes = getItem(position)
        episodes?.let {
            holder.bind(it)
        }
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodesUiModel = getItem(position)
        episodesUiModel?.let {
            when (episodesUiModel) {
                is EpisodesUiModel.Item -> (holder as EpisodesViewHolder).bind(episodesUiModel.episode)
                is EpisodesUiModel.Header -> (holder as SeparatorViewHolder).bind(episodesUiModel.text)

            }
        }
    }



    fun getItemAtPosition(position: Int): EpisodesUiModel? {
        return getItem(position)
    }
    /*fun getItemAtPosition(position: Int): Episode? {
        return getItem(position)
    }*/


    companion object {

        private const val LIST_ITEM_VIEW_TYPE = 0
        private const val HEADER_ITEM_VIEW_TYPE = 1
        private const val INVALID_VIEW_TYPE = -1

       /* private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Episode>() {

            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem == newItem
        }*/




        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<EpisodesUiModel>() {
            override fun areItemsTheSame(oldItem: EpisodesUiModel, newItem: EpisodesUiModel): Boolean =
                when {
                    oldItem is EpisodesUiModel.Item && newItem is EpisodesUiModel.Item -> oldItem.episode.id == newItem.episode.id
                    oldItem is EpisodesUiModel.Header && newItem is EpisodesUiModel.Header -> oldItem.text == newItem.text
                    else -> false
                }

            override fun areContentsTheSame(oldItem: EpisodesUiModel, newItem: EpisodesUiModel): Boolean =
                oldItem == newItem
        }


    }


}
