package com.sisifos.rickyandmortypaging3.ui.search

import com.sisifos.rickyandmortypaging3.domain.mappers.CharacterMapper
import com.sisifos.rickyandmortypaging3.network.NetworkLayer
import com.sisifos.rickyandmortypaging3.domain.models.Character
import androidx.paging.PagingSource
import androidx.paging.PagingState




class CharacterSearchPagingSource(
    private val userSearch: String,
    private val localExceptionCallback: (LocalException) -> Unit
) : PagingSource<Int, Character>() {



    sealed class LocalException(
        val title: String,
        val description: String = ""
    ): Exception() {
        object EmptySearch : LocalException(
            title = "Start typing to search!"
        )
        object NoResults : LocalException(
            title = "Whoops!",
            description = "Looks like your search returned no results"
        )
    }



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {


        if (userSearch.isEmpty()) {
            val exception = LocalException.EmptySearch
            localExceptionCallback(exception)
            return LoadResult.Error(exception)
        }


        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1) null else pageNumber - 1

        val request = NetworkLayer.apiClient.getCharactersPage(
            characterName = userSearch,
            pageIndex = pageNumber
        )


        request.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = request.bodyNullable?.results?.map { characterResponse ->
                CharacterMapper.buildFrom(characterResponse)
            } ?: emptyList(),
            prevKey = previousKey,
            nextKey = getPageIndexFromNext(request.bodyNullable?.info?.next)
        )
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


   /* private fun getPageIndexFromNext(next: String?): Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }
*/


}