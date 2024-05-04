package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.footer

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CharactersLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<CharactersLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CharactersLoadStateViewHolder {
        return CharactersLoadStateViewHolder.create(parent, retry)
    }


    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


}