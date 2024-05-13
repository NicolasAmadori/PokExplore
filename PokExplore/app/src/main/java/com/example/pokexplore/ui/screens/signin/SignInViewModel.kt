package com.example.pokexplore.ui.screens.signin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface SignInActions {
    fun setUser(user: User): Job
    fun login(email: String, password: String, onFinished: () -> Unit = {}): Job
}

class SignInViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    var loggedUser = mutableStateOf<User?>(null)
    private set

    val actions = object : SignInActions {

        override fun setUser(user: User) = viewModelScope.launch {
            dataStoreRepository.setUser(user)
        }

        override fun login(email: String, password: String, onFinished: () -> Unit) = viewModelScope.launch {
            loggedUser = mutableStateOf(databaseRepository.login(email, password))
            onFinished()
        }
    }

}