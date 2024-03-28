package com.sisifos.rickyandmortypaging3.ui.episode.footer

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter


class EpisodeLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<EpisodeLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): EpisodeLoadStateViewHolder {
        return EpisodeLoadStateViewHolder.create(parent, retry)
    }


    override fun onBindViewHolder(holder: EpisodeLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


}