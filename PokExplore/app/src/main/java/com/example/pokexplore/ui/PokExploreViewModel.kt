package com.example.pokexplore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PokemonsState(val pokemons: List<Pokemon>)

interface PokExploreActions {
    fun addUser(user: User): Job
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
        override fun addUser(user: User) = viewModelScope.launch {
            repository.insertUser(user)
        }

//        override fun capture(pokemon: Pokemon) = viewModelScope.launch {
//            repository.capture(pokemon)
//        }
    }
}
