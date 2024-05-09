package com.example.pokexplore.ui.screens.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.repositories.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SignUpState(val user: User?)

class SignUpViewModel(
    private val repository: DataStoreRepository
) : ViewModel()  {
    var state by mutableStateOf(SignUpState(null))
        private set

    fun setUser(user: User) {
        state = SignUpState(user)
        viewModelScope.launch { repository.setUser(user) }
    }

    init {
        viewModelScope.launch {
            state = SignUpState(repository.user.first())
        }
    }
}