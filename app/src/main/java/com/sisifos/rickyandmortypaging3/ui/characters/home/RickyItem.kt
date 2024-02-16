package com.sisifos.rickyandmortypaging3.ui.characters.home

import com.sisifos.rickyandmortypaging3.domain.models.Character
import com.sisifos.rickyandmortypaging3.domain.models.Episode

sealed class RickyItem {
    data class EpisodeItem(val episode: Episode) : RickyItem()
    data class ListItem(val character: Character) : RickyItem()
}

//data class UiCharacter(val id: String, val name: String, val image: String)