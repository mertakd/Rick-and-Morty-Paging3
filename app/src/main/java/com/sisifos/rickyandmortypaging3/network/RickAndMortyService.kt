package com.sisifos.rickyandmortypaging3.network

import com.sisifos.rickyandmortypaging3.common.SimpleResponse
import com.sisifos.rickyandmortypaging3.network.response.GetCharacterByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetCharactersPageResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodeByIdResponse
import com.sisifos.rickyandmortypaging3.network.response.GetEpisodesPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyService {


    @GET("character/")
    suspend fun getAllCharactersPage(
        @Query("name") characterName: String,
        @Query("page") pageIndex: Int
    ):GetCharactersPageResponse



    @GET("character/{character-id}")
    suspend fun getCharacterById(
        @Path("character-id") characterId: Int
    ):GetCharacterByIdResponse


    @GET("character/{list}")
    suspend fun getMultipleCharacters(
        @Path("list") characterList: List<String>
    ):List<GetCharacterByIdResponse>





    @GET("episode/{episode-id}")
    suspend fun getEpisodeById(
        @Path("episode-id") episodeId: Int
    ):GetEpisodeByIdResponse

    @GET("episode/{episode-range}")
    suspend fun getEpisodeRange(
        @Path("episode-range") episodeRange: String
    ):List<GetEpisodeByIdResponse>

    @GET("episode/")
    suspend fun getEpisodesPage(
        @Query("page") pageIndex: Int
    ):GetEpisodesPageResponse
}