package com.sisifos.rickyandmortypaging3.ui.characters.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.sisifos.rickyandmortypaging3.Constants
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersUiModel
import kotlinx.coroutines.flow.map

class CharactersViewModel: ViewModel(){

    private val repository = CharactersRepository()

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = Constants.PREFETCH_DISTANCE,
            enablePlaceholders = false
        )
    ) {
        CharactersPagingSource(repository)
    }.flow.cachedIn(viewModelScope).map { pagingData ->
        pagingData.insertSeparators { before: CharactersUiModel?, after: CharactersUiModel? ->

            // Initial separator for the first main characters header (before the whole list)
            if (before == null) {
                return@insertSeparators CharactersUiModel.Header("Main Characters")
            }

            // No footer
            if (after == null) {
                return@insertSeparators null
            }

            // Make sure we only care about the items (characters)
            if (before is CharactersUiModel.Header || after is CharactersUiModel.Header) {
                return@insertSeparators null
            }

            // Get the characters from the items
            val character1 = (before as CharactersUiModel.Item).character
            val character2 = (after as CharactersUiModel.Item).character

            // Check if a separator is necessary
            return@insertSeparators if (character2.id <= 5) {
                null // No separator for the first 5 characters
            } else if (character1.id <= 5) {
                CharactersUiModel.Header("Other Characters")
            } else {
                null
            }
        }
    }


}