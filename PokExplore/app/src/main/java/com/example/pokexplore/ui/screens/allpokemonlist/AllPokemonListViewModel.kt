package com.example.pokexplore.ui.screens.allpokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AllPokemonListState(val pokemonList: List<Pokemon>)

class AllPokemonListViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    val state = databaseRepository.pokemons.map { AllPokemonListState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AllPokemonListState(emptyList())
    )
}