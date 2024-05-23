package com.example.pokexplore.ui.screens.settings

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

data class UserState(val user: User?)

interface ChangePasswordActions {
    fun setPassword(email: String, password: String): Job
}

class ChangePasswordViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var userState by mutableStateOf(UserState(null))
        private set

    val actions = object : ChangePasswordActions {
        override fun setPassword(email: String, password: String) = viewModelScope.launch {
            databaseRepository.setPassword(email, password)
        }
    }

    init {
        viewModelScope.launch {
            userState = UserState(dataStoreRepository.user.firstOrNull())
        }
    }
}