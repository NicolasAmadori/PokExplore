package com.example.pokexplore.ui.screens.loading

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.PokExploreRepository
import com.example.pokexplore.utilities.parseJsonToPokemonList
import com.example.pokexplore.utilities.readJsonFromAssets
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LoadingState(val pokemonList: List<Pokemon>)

interface LoadingActions {
    fun downloadPokemons(context: Context, onFinished: () -> Unit): Job
}

class LoadingViewModel(
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    val state = databaseRepository.pokemons.map { LoadingState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LoadingState(emptyList())
    )

    val actions = object : LoadingActions {
        override fun downloadPokemons(context: Context, onFinished: () -> Unit) = viewModelScope.launch {
            val jsonString = readJsonFromAssets(context, "pokemon_data.json")
            val pokemonList = parseJsonToPokemonList(jsonString)
            databaseRepository.upsert(pokemonList)
            onFinished()
        }
    }
}