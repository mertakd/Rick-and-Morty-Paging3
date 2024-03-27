package com.sisifos.rickyandmortypaging3.ui.characters.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    private val savedStateHandle: SavedStateHandle
) :ViewModel() {


    val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<UiModel>>

    val accept: (UiAction) -> Unit


    init {
        val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>() //actionStateFlow adında bir paylaşılan akış oluşturulur. Bu akış, UI tarafından gönderilen eylemleri (UiAction) içerir.
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchRepo(queryString = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,

                // If the search query matches the scroll query, the user has scrolled
                // Arama sorgusu kaydırma sorgusuyla eşleşiyorsa kullanıcı kaydırmıştır
                //search nesnesinin query özelliği, scroll nesnesinin currentQuery özelliğine eşit değilse, hasNotScrolledForCurrentSearch değeri true olur; aksi takdirde, yani bu iki özellik eşitse, hasNotScrolledForCurrentSearch değeri false olur.
                //hasNotScrolledForCurrentSearch değeri uygulama ilk açıldığında true olur. yani kulanıcı listeyi scroll etmemiştir. çünkü her iki değerin default değeri android.
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

    }





    private fun searchRepo(queryString: String): Flow<PagingData<UiModel>> =
        repository.getSearchResultStream(queryString)
            .map { pagingData -> pagingData.map { UiModel.CharacterItem(it) } }
            .map {
                it.insertSeparators { before: UiModel?, after: UiModel? ->

                    // Initial separator for the first main characters header (before the whole list)
                    if (before == null) {
                        return@insertSeparators UiModel.SeparatorItem("Main Characters")
                    }

                    // No footer
                    if (after == null) {
                        return@insertSeparators null
                    }

                    // Make sure we only care about the items (characters)
                    if (before is UiModel.SeparatorItem || after is UiModel.SeparatorItem) {
                        return@insertSeparators null
                    }

                    // Get the characters from the items
                    val character1 = (before as UiModel.CharacterItem).character
                    val character2 = (after as UiModel.CharacterItem).character

                    // Check if a separator is necessary
                    return@insertSeparators if (character2.id <= 5) {
                        null // No separator for the first 5 characters
                    } else if (character1.id <= 5) {
                        UiModel.SeparatorItem("Other Characters")
                    } else {
                        null
                    }
                }
            }





    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }




}





sealed class UiModel {
    data class CharacterItem(val character: Character): UiModel()
    data class SeparatorItem(val text: String): UiModel()
}




sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()

}


data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)







private const val LAST_SEARCH_QUERY: String = "last_search_query"
const val DEFAULT_QUERY = ""
private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"