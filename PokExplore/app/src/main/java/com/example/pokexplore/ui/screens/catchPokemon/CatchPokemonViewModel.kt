package com.example.pokexplore.ui.screens.catchPokemon

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
data class CountryCodeState(val countryCode: String?)

interface CatchPokemonActions {
    fun capturePokemon(email: String, pokemonId: Int): Job
}

class CatchPokemonViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var state = databaseRepository.userPokemons.map { PokemonState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PokemonState(emptyList())
    )

    var userState by mutableStateOf(UserState(null))
        private set

    var countryCodeState = dataStoreRepository.countryCode.map { CountryCodeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CountryCodeState(null)
    )

    val actions = object : CatchPokemonActions {
        override fun capturePokemon(email: String, pokemonId: Int) = viewModelScope.launch {
            databaseRepository.capturePokemon(email, pokemonId)
        }
    }

    init {
        viewModelScope.launch {
            userState = UserState(dataStoreRepository.user.firstOrNull())
        }
    }
}