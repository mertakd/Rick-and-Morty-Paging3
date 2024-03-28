package com.sisifos.rickyandmortypaging3.ui.episode


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
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


    private val _state = MutableStateFlow(EpisodeListState())
    val state: StateFlow<EpisodeListState> get() = _state


    private val episodeFlow: Flow<PagingData<EpisodesUiModel>> by lazy {
        getAllEpisodeStream()
            .cachedIn(viewModelScope)
    }

    init {
        viewModelScope.launch {
            episodeFlow.collect {
                _state.value = EpisodeListState(it)
            }
        }
    }










    private fun getAllEpisodeStream(): Flow<PagingData<EpisodesUiModel>> =
        repository.getAllEpisodeStream().map { pagingData ->
            pagingData.insertSeparators { model, nextModel ->
                if (model == null) {
                    EpisodesUiModel.Header("Season 1")
                } else if (nextModel == null) {
                    null
                } else if (model is EpisodesUiModel.Header || nextModel is EpisodesUiModel.Header) {
                    null
                } else {
                    val episode1 = (model as EpisodesUiModel.Item).episode
                    val episode2 = (nextModel as EpisodesUiModel.Item).episode
                    if (episode2.seasonNumber != episode1.seasonNumber) {
                        EpisodesUiModel.Header("Season ${episode2.seasonNumber}")
                    } else {
                        null
                    }
                }
            }
        }









}








sealed class EpisodesUiModel {
    data class Item(val episode: Episode): EpisodesUiModel()
    data class Header(val text: String): EpisodesUiModel()
}



data class EpisodeListState(val episodeList: PagingData<EpisodesUiModel>? = PagingData.empty() ) {}






