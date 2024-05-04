package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.sisifos.rickyandmortypaging3.R
import com.sisifos.rickyandmortypaging3.databinding.CharactersLoadStateFooterViewItemBinding

class CharactersLoadStateViewHolder(
    private val binding: CharactersLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {

        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.characters_load_state_footer_view_item, parent, false)
            val binding = CharactersLoadStateFooterViewItemBinding.bind(view)
            return CharactersLoadStateViewHolder(binding, retry)
        }
    }
}