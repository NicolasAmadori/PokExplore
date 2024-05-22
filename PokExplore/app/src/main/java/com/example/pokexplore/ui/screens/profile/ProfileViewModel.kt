package com.example.pokexplore.ui.screens.profile

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

data class PokemonState(val userWithPokemonsList: List<UserWithPokemons>)
data class UserState(val user: User?)

interface ProfileActions {
    fun logOut(): Job
    fun toggleFavourite(email: String, pokemonId: Int): Job
}


class ProfileViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var userState by mutableStateOf(UserState(null))
        private set

    var pokemonsState = databaseRepository.userPokemons.map { PokemonState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonState(emptyList())
    )

    val actions = object : ProfileActions {
        override fun logOut(): Job {
            userState = UserState(null)
            return viewModelScope.launch { dataStoreRepository.removeUser() }
        }

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