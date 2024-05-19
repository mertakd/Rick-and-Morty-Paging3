package com.sisifos.rickyandmortypaging3.ui.characters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
): ViewModel() {



    private val _characterFlow = MutableStateFlow<Character?>(null)
    val characterFlow: Flow<Character?> = _characterFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        val character = repository.getCharacterById(characterId)
        _characterFlow.value = character
    }
}