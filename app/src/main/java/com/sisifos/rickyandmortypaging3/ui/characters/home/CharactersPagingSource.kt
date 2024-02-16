package com.sisifos.rickyandmortypaging3.ui.characters.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sisifos.rickyandmortypaging3.domain.mappers.CharacterMapper
import com.sisifos.rickyandmortypaging3.domain.mappers.EpisodeMapper
import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.network.NetworkLayer
import com.sisifos.rickyandmortypaging3.ui.episode.EpisodesUiModel
import com.sisifos.rickyandmortypaging3.ui.characters.home.adapter.CharactersUiModel

class CharactersPagingSource(
    private val repository: CharactersRepository
): PagingSource<Int, CharactersUiModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersUiModel> {
        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1)null else pageNumber.minus(1)

        val pageRequest = NetworkLayer.apiClient.getCharactersPage(pageNumber)
        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = pageRequest.body.results.map { response ->
                CharactersUiModel.Item(CharacterMapper.buildFrom(response))
            },
            prevKey = previousKey,
            nextKey = getPageIndexFromNext(pageRequest.body.info.next)
        )
    }



    override fun getRefreshKey(state: PagingState<Int, CharactersUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    private fun getPageIndexFromNext(next: String?): Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }
}