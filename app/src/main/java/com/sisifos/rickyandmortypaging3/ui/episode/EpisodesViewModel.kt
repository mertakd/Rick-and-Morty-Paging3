package com.sisifos.rickyandmortypaging3.ui.episode


import androidx.lifecycle.SavedStateHandle
import com.sisifos.rickyandmortypaging3.common.Constants
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import com.sisifos.rickyandmortypaging3.ui.characters.home.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {











    fun getAllEpisodeStream(): Flow<PagingData<EpisodesUiModel>> =
        repository.getAllEpisodeStream().map {
            it.insertSeparators{model: EpisodesUiModel?, model2: EpisodesUiModel? ->
                // Initial separator for the first season header (before the whole list)
                if (model == null) {
                    return@insertSeparators EpisodesUiModel.Header("Season 1")
                }

                // No footer
                if (model2 == null) {
                    return@insertSeparators null
                }

                // Make sure we only care about the items (episodes)
                if (model is EpisodesUiModel.Header || model2 is EpisodesUiModel.Header) {
                    return@insertSeparators null
                }

                // Little logic to determine if a separator is necessary
                val episode1 = (model as EpisodesUiModel.Item).episode
                val episode2 = (model2 as EpisodesUiModel.Item).episode
                return@insertSeparators if (episode2.seasonNumber != episode1.seasonNumber) {
                    EpisodesUiModel.Header("Season ${episode2.seasonNumber}")
                } else {
                    null
                }
            }
        }


}








sealed class EpisodesUiModel {
    data class Item(val episode: Episode): EpisodesUiModel()
    data class Header(val text: String): EpisodesUiModel()
}

