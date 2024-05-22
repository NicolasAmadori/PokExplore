package com.example.pokexplore.ui.screens.allpokemonlist

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

data class AllPokemonListState(val userWithPokemonsList: List<UserWithPokemons>)
data class UserState(val user: User?)
data class CountryCodeState(val countryCode: String?)

interface AllPokemonListActions {
    fun toggleFavourite(email: String, pokemonId: Int): Job

    fun setCountryCode(countryCode: String): Job
}

class AllPokemonListViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var state = databaseRepository.userPokemons.map { AllPokemonListState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AllPokemonListState(emptyList())
    )

    var userState by mutableStateOf(UserState(null))
        private set

    var countryCodeState = dataStoreRepository.countryCode.map { CountryCodeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CountryCodeState(null)
    )

    val actions = object : AllPokemonListActions {
        override fun toggleFavourite(email: String, pokemonId: Int) = viewModelScope.launch {
            databaseRepository.toggleFavorite(email, pokemonId)
        }

        override fun setCountryCode(countryCode: String) = viewModelScope.launch {
            dataStoreRepository.setCountryCode(countryCode)
        }
    }

    init {
        viewModelScope.launch {
            userState = UserState(dataStoreRepository.user.firstOrNull())
        }
    }
}