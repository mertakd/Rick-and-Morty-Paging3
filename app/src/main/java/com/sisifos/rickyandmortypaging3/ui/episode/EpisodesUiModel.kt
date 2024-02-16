package com.sisifos.rickyandmortypaging3.ui.episode

import com.sisifos.rickyandmortypaging3.domain.models.Episode

sealed class EpisodesUiModel {
    data class Item(val episode: Episode): EpisodesUiModel()
    data class Header(val text: String): EpisodesUiModel()
}