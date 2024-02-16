package com.sisifos.rickyandmortypaging3.network

import com.sisifos.rickyandmortypaging3.domain.models.Character


object SimpleMortyCache {

    val characterMap = mutableMapOf<Int, Character>()
}