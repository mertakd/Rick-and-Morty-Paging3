package com.sisifos.rickyandmortypaging3.ui.characters.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sisifos.rickyandmortypaging3.domain.mappers.CharacterMapper
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.network.RickAndMortyService


class CharactersPagingSource(
    private val service: RickAndMortyService,
    private val userSearch: String,
): PagingSource<Int, Character>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        try {
            val pageNumber = params.key ?: 1
            val previousKey = if (pageNumber == 1) null else pageNumber - 1

            val charactersResponse = service.getAllCharactersPage(
                characterName = userSearch,
                pageIndex = pageNumber
            )

            val characters = charactersResponse.results?.map { response ->
                CharacterMapper.buildFrom(response)
            } ?: emptyList()

            return LoadResult.Page(
                data = characters,
                prevKey = previousKey,
                nextKey = getPageIndexFromNext(charactersResponse.info.next)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }



    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }



    private fun getPageIndexFromNext(next: String?): Int? {
        if (next == null) {
            return null
        }

        val remainder = next.substringAfter("page=")
        val finalIndex = if (remainder.contains('&')) {
            remainder.indexOfFirst { it == '&' }
        } else {
            remainder.length
        }

        return remainder.substring(0, finalIndex).toIntOrNull()
    }


}