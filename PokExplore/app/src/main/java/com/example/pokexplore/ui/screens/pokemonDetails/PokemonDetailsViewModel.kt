package com.example.pokexplore.ui.screens.pokemonDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class PokemonDetailsState(val pokemonList: List<Pokemon>)

class PokemonDetailsViewModel(
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    val state = databaseRepository.pokemons.map { PokemonDetailsState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonDetailsState(emptyList())
    )
}