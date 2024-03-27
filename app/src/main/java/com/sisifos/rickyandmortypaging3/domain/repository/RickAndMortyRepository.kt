package com.sisifos.rickyandmortypaging3.domain.repository


import androidx.paging.PagingData
import com.sisifos.rickyandmortypaging3.common.SimpleResponse
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.network.response.GetCharacterByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetCharactersPageResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodeByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodesPageResponse
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel
import kotlinx.coroutines.flow.Flow



interface RickAndMortyRepository {

    //CHARACTER
    fun getSearchResultStream(query: String): Flow<PagingData<Character>>

    suspend fun getCharacterById(characterId: Int): Character

    suspend fun getEpisodesFromCharacterResponse(characterResponse: GetCharacterByIdResponse): List<GetEpisodeByIdResponse>





    //EPISODE
    fun getAllEpisodeStream(): Flow<PagingData<EpisodesUiModel>>

    suspend fun getEpisodeById(episodeId: Int): Episode

    suspend fun getCharactersFromEpisodeResponse(
        episodeByIdResponse: GetEpisodeByIdResponse
    ): List<GetCharacterByIdResponse>



}
