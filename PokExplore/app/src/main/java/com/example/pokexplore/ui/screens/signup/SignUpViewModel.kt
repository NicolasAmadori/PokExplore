package com.example.pokexplore.ui.screens.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.database.UserPokemon
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


data class UsersState(val users: List<User>)

interface SignUpActions {
    fun setUser(user: User): Job
    fun signUpUser(user: User, onFinished: () -> Unit): Job
}

class SignUpViewModel(
    private val repository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var loggedUser = mutableStateOf<User?>(null)
        private set

    var usersState by mutableStateOf(UsersState(listOf()))
        private set

    val actions = object : SignUpActions {

        override fun setUser(user: User) = viewModelScope.launch {
            repository.setUser(user)
        }

        override fun signUpUser(user: User, onFinished: () -> Unit) = viewModelScope.launch {
            val userPokemonList = mutableListOf<UserPokemon>()
            val pokemons = databaseRepository.pokemons.first()

            pokemons.forEach{pokemon->
                userPokemonList.add(
                    UserPokemon(
                    email = user.email,
                    pokemonId = pokemon.pokemonId
                )
                )
            }
            loggedUser = mutableStateOf(user)
            databaseRepository.insertUser(user) //Save user in the database
            databaseRepository.insertUserPokemon(userPokemonList.toList())
            repository.setUser(user) //Save user in DataStore
            onFinished()
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = mutableStateOf(repository.user.first())
            usersState = UsersState(databaseRepository.users.first())
        }
    }
}