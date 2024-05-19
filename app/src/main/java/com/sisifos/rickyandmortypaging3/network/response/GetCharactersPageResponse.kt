package com.sisifos.rickyandmortypaging3.network.response

data class GetCharactersPageResponse(
    val info: PageInfo = PageInfo(),
    val results: List<GetCharacterByIdResponse>? = emptyList()
)