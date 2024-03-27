package com.sisifos.rickyandmortypaging3.ui.episode.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
): ViewModel() {




    private var _episodeLiveData = MutableLiveData<Episode>()
    val episodeLiveData: LiveData<Episode> = _episodeLiveData


    fun fetchEpisode(episodeId: Int) {
        viewModelScope.launch {
            try {
                val episode = repository.getEpisodeById(episodeId)
                _episodeLiveData.postValue(episode)
            } catch (e: Exception) {
                Log.e("EpisodeDetailViewModel", "Error fetching episode: $e")
            }
        }
    }

}