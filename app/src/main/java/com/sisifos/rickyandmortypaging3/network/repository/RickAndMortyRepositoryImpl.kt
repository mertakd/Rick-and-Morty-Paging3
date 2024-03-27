package com.sisifos.rickyandmortypaging3.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sisifos.rickyandmortypaging3.common.Constants
import com.sisifos.rickyandmortypaging3.domain.mappers.CharacterMapper
import com.sisifos.rickyandmortypaging3.domain.mappers.EpisodeMapper
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.domain.repository.RickAndMortyRepository
import com.sisifos.rickyandmortypaging3.network.SimpleMortyCache
import com.sisifos.rickyandmortypaging3.network.response.GetCharacterByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodeByIdResponse
import com.sisifos.rickyandmortypaging3.network.RickAndMortyService
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodesPageResponse
import com.sisifos.rickyandmortypaging3.ui.characters.home.CharactersPagingSource
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodePagingSource
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val rickAndMortyService: RickAndMortyService,
): RickAndMortyRepository {


    override fun getSearchResultStream(query: String): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CharactersPagingSource(rickAndMortyService,query) }
        ).flow
    }




    override suspend fun getCharacterById(characterId: Int): Character {
        val cachedCharacter = SimpleMortyCache.characterMap[characterId]
        if (cachedCharacter != null) {
            return cachedCharacter
        }

        val request = rickAndMortyService.getCharacterById(characterId)

        val networkEpisodes = getEpisodesFromCharacterResponse(request)

        val character = CharacterMapper.buildFrom(
            response = request,
            episodes = networkEpisodes
        )

        // Update cache & return value
        SimpleMortyCache.characterMap[characterId] = character
        return character


    }

    override suspend fun getEpisodesFromCharacterResponse(characterResponse: GetCharacterByIdResponse): List<GetEpisodeByIdResponse> {
        val episodeRange = characterResponse.episode.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }.toString()

        return rickAndMortyService.getEpisodeRange(episodeRange)
    }




    //EPISODE
    override fun getAllEpisodeStream(): Flow<PagingData<EpisodesUiModel>> {
        return Pager(
            PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = Constants.PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {EpisodePagingSource(rickAndMortyService)}
        ).flow

    }


    override suspend fun getEpisodeById(episodeId: Int): Episode {
        val request = rickAndMortyService.getEpisodeById(episodeId)

        val characterList = getCharactersFromEpisodeResponse(request)

        return EpisodeMapper.buildFrom(
            networkEpisode = request,
            networkCharacters = characterList
        )
    }

    override suspend fun getCharactersFromEpisodeResponse(episodeByIdResponse: GetEpisodeByIdResponse): List<GetCharacterByIdResponse> {
        val characterList = episodeByIdResponse.characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }
        return rickAndMortyService.getMultipleCharacters(characterList)
    }


}