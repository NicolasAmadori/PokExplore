package com.example.pokexplore.ui.screens.caughtPokemons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.database.UserWithPokemons
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CaughtPokemonState(val userWithPokemonsList: List<UserWithPokemons>)
data class UserState(val user: User?)

interface CaughtPokemonsActions {
    fun toggleFavourite(email: String, pokemonId: Int): Job
}

class CaughtPokemonsViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var state = databaseRepository.userPokemons.map { CaughtPokemonState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CaughtPokemonState(emptyList())
    )

    var userState by mutableStateOf(UserState(null))
        private set

    val actions = object : CaughtPokemonsActions {
        override fun toggleFavourite(email: String, pokemonId: Int) = viewModelScope.launch {
            databaseRepository.toggleFavorite(email, pokemonId)
        }
    }

    init {
        viewModelScope.launch {
            userState = UserState(dataStoreRepository.user.firstOrNull())
        }
    }
}