package com.sisifos.rickyandmortypaging3.ui.episode.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.EpisodeLoadStateFooterViewItemBinding

class EpisodeLoadStateViewHolder(
    private val binding: EpisodeLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButtonEpisode.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {

        if (loadState is LoadState.Error) {
            binding.errorMsgEpisode.text = loadState.error.localizedMessage
        }
        binding.progressBarEpisode.isVisible = loadState is LoadState.Loading
        binding.retryButtonEpisode.isVisible = loadState is LoadState.Error
        binding.errorMsgEpisode.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): EpisodeLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.episode_load_state_footer_view_item, parent, false)
            val binding = EpisodeLoadStateFooterViewItemBinding.bind(view)
            return EpisodeLoadStateViewHolder(binding, retry)
        }
    }
}