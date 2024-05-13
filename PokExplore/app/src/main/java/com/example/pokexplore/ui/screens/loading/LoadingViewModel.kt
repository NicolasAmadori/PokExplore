package com.example.pokexplore.ui.screens.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LoadingState(val pokemonList: List<Pokemon>)

interface LoadingActions {
    fun downloadPokemons(onFinished: () -> Unit): Job
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
        override fun downloadPokemons(onFinished: () -> Unit) = viewModelScope.launch {
            val bulbasaur = Pokemon(
                pokemonId = 1,
                name = "Bulbasaur",
                sprite = "bulbasaur_sprite.png",
                types = arrayListOf("grass", "poison"),
                abilities = arrayListOf("Overgrow", "Chlorophyll"),
                weight = 69,
                height = 7,
                stats = mapOf(
                    "HP" to 45,
                    "Attack" to 49,
                    "Defense" to 49,
                    "Special Attack" to 65,
                    "Special Defense" to 65,
                    "Speed" to 45
                ),
                cry = "bulbasaur_cry.wav",
                description = "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
                captureRate = 45,
                evolutions = arrayListOf("Ivysaur"),
                generation = "First",
                countryCode = 1
            )

            val charmander = Pokemon(
                pokemonId = 4,
                name = "Charmander",
                sprite = "charmander_sprite.png",
                types = arrayListOf("fire"),
                abilities = arrayListOf("Blaze", "Solar Power"),
                weight = 85,
                height = 6,
                stats = mapOf(
                    "HP" to 39,
                    "Attack" to 52,
                    "Defense" to 43,
                    "Special Attack" to 60,
                    "Special Defense" to 50,
                    "Speed" to 65
                ),
                cry = "charmander_cry.wav",
                description = "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
                captureRate = 45,
                evolutions = arrayListOf("Charmeleon"),
                generation = "First",
                countryCode = 1
            )

            val squirtle = Pokemon(
                pokemonId = 7,
                name = "Squirtle",
                sprite = "squirtle_sprite.png",
                types = arrayListOf("water"),
                abilities = arrayListOf("Torrent", "Rain Dish"),
                weight = 90,
                height = 5,
                stats = mapOf(
                    "HP" to 44,
                    "Attack" to 48,
                    "Defense" to 65,
                    "Special Attack" to 50,
                    "Special Defense" to 64,
                    "Speed" to 43
                ),
                cry = "squirtle_cry.wav",
                description = "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.",
                captureRate = 45,
                evolutions = arrayListOf("Wartortle"),
                generation = "First",
                countryCode = 1
            )
            databaseRepository.upsert(squirtle)
            databaseRepository.upsert(bulbasaur)
            databaseRepository.upsert(charmander)
            onFinished()
        }
    }
}