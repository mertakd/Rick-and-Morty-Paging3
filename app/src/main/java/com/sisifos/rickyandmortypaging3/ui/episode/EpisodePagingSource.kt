package com.sisifos.rickyandmortypaging3.ui.episode

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sisifos.rickyandmortypaging3.domain.mappers.EpisodeMapper
import com.sisifos.rickyandmortypaging3.domain.models.Episode
import com.sisifos.rickyandmortypaging3.network.NetworkLayer

class EpisodePagingSource(
    private val repository: EpisodeRepository
): PagingSource<Int, EpisodesUiModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesUiModel> {
        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1) null else pageNumber.minus(1)

        val pageRequest = NetworkLayer.apiClient.getEpisodesPage(pageNumber)

        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = pageRequest.body.results.map { response ->
                 EpisodesUiModel.Item(EpisodeMapper.buildFrom(response))
            },
            prevKey = previousKey,
            nextKey = getPageIndexFromNext(pageRequest.body.info.next)

        )
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