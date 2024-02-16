package com.sisifos.rickyandmortypaging3.ui.characters.home.adapter

import com.sisifos.rickyandmortypaging3.domain.models.Character


sealed class CharactersUiModel {
    data class Item(val character: Character): CharactersUiModel()
    data class Header(val text: String): CharactersUiModel()
}