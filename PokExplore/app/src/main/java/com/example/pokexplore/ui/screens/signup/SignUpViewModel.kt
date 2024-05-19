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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SignUpState(val user: User?)

data class UsersState(val users: List<User>)

class SignUpViewModel(
    private val repository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var state by mutableStateOf(SignUpState(null))
        private set

//    var loggedUser = mutableStateOf<User?>(null)
//        private set

    var usersState by mutableStateOf(UsersState(listOf()))
        private set

    fun signUpUser(user: User, onFinished: () -> Unit) {
        state = SignUpState(user)
        viewModelScope.launch {
            val userPokemonList = mutableListOf<UserPokemon>()
            val pokemons = databaseRepository.pokemons.first()

            pokemons.forEach{pokemon->
                userPokemonList.add(UserPokemon(
                    email = user.email,
                    pokemonId = pokemon.pokemonId
                ))
            }
//            loggedUser = mutableStateOf(user)
            databaseRepository.insertUser(user) //Save user in the database
            databaseRepository.insertUserPokemon(userPokemonList.toList())
            repository.setUser(user) //Save user in DataStore
        }
        onFinished()
    }

    init {
        viewModelScope.launch {
            state = SignUpState(repository.user.first())
            usersState = UsersState(databaseRepository.users.first())
        }
    }
}