package com.example.pokexplore.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.repositories.DataStoreRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ProfileState(val user: User?)

class ProfileViewModel(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel()  {
    var state by mutableStateOf(ProfileState(null))
        private set

    fun logOut() {
        state = ProfileState(null)
        viewModelScope.launch { dataStoreRepository.removeUser() }
    }

    init {
        viewModelScope.launch {
            state = ProfileState(dataStoreRepository.user.firstOrNull())
        }
    }
}