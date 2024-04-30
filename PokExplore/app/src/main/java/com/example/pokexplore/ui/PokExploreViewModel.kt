package com.example.pokexplore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonsState(val pokemons: List<Pokemon>)

interface PokExploreActions {
    fun addPokemon(pokemon: Pokemon): Job
//    fun capture(pokemon: Pokemon): Job
}

class PokExploreViewModel(
    private val repository: PokExploreRepository
) : ViewModel() {
    val state = repository.pokemons.map { PokemonsState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonsState(emptyList())
    )

    val actions = object : PokExploreActions {
        override fun addPokemon(pokemon: Pokemon) = viewModelScope.launch {
            repository.upsert(pokemon)
        }

//        override fun capture(pokemon: Pokemon) = viewModelScope.launch {
//            repository.capture(pokemon)
//        }
    }
}
