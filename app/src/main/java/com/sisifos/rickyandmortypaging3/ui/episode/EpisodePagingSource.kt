package com.sisifos.rickyandmortypaging3.ui.episode

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sisifos.rickyandmortypaging3.domain.mappers.CharacterMapper
import com.sisifos.rickyandmortypaging3.domain.mappers.EpisodeMapper
import com.sisifos.rickyandmortypaging3.network.RickAndMortyService


private const val GITHUB_STARTING_PAGE_INDEX = 1
class EpisodePagingSource(
    private val rickAndMortyService: RickAndMortyService
): PagingSource<Int, EpisodesUiModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesUiModel> {

        try {
            val pageNumber = params.key ?: GITHUB_STARTING_PAGE_INDEX
            val previousKey = if (pageNumber == 1) null else pageNumber - 1


            val episodeResponse = rickAndMortyService.getEpisodesPage(
                pageIndex = pageNumber
            )

            val characters = episodeResponse.results?.map { response ->
                EpisodesUiModel.Item(EpisodeMapper.buildFrom(response))
            } ?: emptyList()

            return LoadResult.Page(
                data = characters,
                prevKey = previousKey,
                nextKey = getPageIndexFromNext(episodeResponse.info.next)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }






    override fun getRefreshKey(state: PagingState<Int, EpisodesUiModel>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }





    private fun getPageIndexFromNext(next: String?): Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }




}