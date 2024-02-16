package com.sisifos.rickyandmortypaging3.ui.episode

import com.sisifos.rickyandmortypaging3.domain.mappers.EpisodeMapper
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.network.NetworkLayer
import com.sisifos.rickyandmortypaging3.network.response.GetCharacterByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodeByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodesPageResponse

class EpisodeRepository {


    suspend fun fetchEpisodesPage(pageIndex: Int): GetEpisodesPageResponse? {
        val pageRequest = NetworkLayer.apiClient.getEpisodesPage(pageIndex)

        if (!pageRequest.isSuccessful) {
            return null
        }

        return pageRequest.body
    }



    suspend fun getEpisodeById(episodeId: Int): Episode? {

        val request = NetworkLayer.apiClient.getEpisodeById(episodeId)

        if (!request.isSuccessful) {
            return null
        }

        val characterList = getCharactersFromEpisodeResponse(request.body)

        return EpisodeMapper.buildFrom(
            networkEpisode = request.body,
            networkCharacters = characterList
        )
    }




    private suspend fun getCharactersFromEpisodeResponse(
        episodeByIdResponse: GetEpisodeByIdResponse
    ): List<GetCharacterByIdResponse> {
        val characterList = episodeByIdResponse.characters.map {
            it.substring(startIndex = it.lastIndexOf("/") + 1)
        }

        val request = NetworkLayer.apiClient.getMultipleCharacters(characterList)
        return request.bodyNullable ?: emptyList()
    }




}