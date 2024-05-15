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
//            val bulbasaur = Pokemon(
//                pokemonId = 1,
//                name = "bulbasaur",
//                types = listOf("grass", "poison"),
//                abilities = listOf("overgrow", "chlorophyll"),
//                weight = 69,
//                height = 7,
//                stats = mapOf(
//                    "hp" to 45,
//                    "attack" to 49,
//                    "defense" to 49,
//                    "special-attack" to 65,
//                    "special-defense" to 65,
//                    "speed" to 45
//                ),
//                description = "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
//                captureRate = 45,
//                evolutions = listOf("Ivysaur", "Venusaur"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val ivysaur = Pokemon(
//                pokemonId = 2,
//                name = "ivysaur",
//                types = listOf("grass", "poison"),
//                abilities = listOf("overgrow", "chlorophyll"),
//                weight = 130,
//                height = 10,
//                stats = mapOf(
//                    "hp" to 60,
//                    "attack" to 62,
//                    "defense" to 63,
//                    "special-attack" to 80,
//                    "special-defense" to 80,
//                    "speed" to 60
//                ),
//                description = "When the bulb on its back grows large, it appears to lose the ability to stand on its hind legs.",
//                captureRate = 45,
//                evolutions = listOf("Venusaur"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val venusaur = Pokemon(
//                pokemonId = 3,
//                name = "venusaur",
//                types = listOf("grass", "poison"),
//                abilities = listOf("overgrow", "chlorophyll"),
//                weight = 1000,
//                height = 20,
//                stats = mapOf(
//                    "hp" to 80,
//                    "attack" to 82,
//                    "defense" to 83,
//                    "special-attack" to 100,
//                    "special-defense" to 100,
//                    "speed" to 80
//                ),
//                description = "The plant blooms when it is absorbing solar energy. It stays on the move to seek sunlight.",
//                captureRate = 45,
//                evolutions = emptyList(),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val charmander = Pokemon(
//                pokemonId = 4,
//                name = "charmander",
//                types = listOf("fire"),
//                abilities = listOf("blaze", "solar Power"),
//                weight = 85,
//                height = 6,
//                stats = mapOf(
//                    "hp" to 39,
//                    "attack" to 52,
//                    "defense" to 43,
//                    "special-attack" to 60,
//                    "special-defense" to 50,
//                    "speed" to 65
//                ),
//                description = "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
//                captureRate = 45,
//                evolutions = listOf("Charmeleon", "Charizard"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val charmeleon = Pokemon(
//                pokemonId = 5,
//                name = "charmeleon",
//                types = listOf("fire"),
//                abilities = listOf("blaze", "solar Power"),
//                weight = 190,
//                height = 11,
//                stats = mapOf(
//                    "hp" to 58,
//                    "attack" to 64,
//                    "defense" to 58,
//                    "special-attack" to 80,
//                    "special-defense" to 65,
//                    "speed" to 80
//                ),
//                description = "When it swings its burning tail, it elevates the temperature to unbearably high levels.",
//                captureRate = 45,
//                evolutions = listOf("Charizard"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val charizard = Pokemon(
//                pokemonId = 6,
//                name = "charizard",
//                types = listOf("fire", "flying"),
//                abilities = listOf("blaze", "solar Power"),
//                weight = 905,
//                height = 17,
//                stats = mapOf(
//                    "hp" to 78,
//                    "attack" to 84,
//                    "defense" to 78,
//                    "special-attack" to 109,
//                    "special-defense" to 85,
//                    "speed" to 100
//                ),
//                description = "Its wings can carry this Pokémon close to an altitude of 4,600 feet. It blows out fire at very high temperatures.",
//                captureRate = 45,
//                evolutions = emptyList(),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val squirtle = Pokemon(
//                pokemonId = 7,
//                name = "squirtle",
//                types = listOf("water"),
//                abilities = listOf("torrent", "rain Dish"),
//                weight = 90,
//                height = 5,
//                stats = mapOf(
//                    "hp" to 44,
//                    "attack" to 48,
//                    "defense" to 65,
//                    "special-attack" to 50,
//                    "special-defense" to 64,
//                    "speed" to 43
//                ),
//                description = "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.",
//                captureRate = 45,
//                evolutions = listOf("Wartortle", "Blastoise"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val wartortle = Pokemon(
//                pokemonId = 8,
//                name = "wartortle",
//                types = listOf("water"),
//                abilities = listOf("torrent", "rain Dish"),
//                weight = 225,
//                height = 10,
//                stats = mapOf(
//                    "hp" to 59,
//                    "attack" to 63,
//                    "defense" to 80,
//                    "special-attack" to 65,
//                    "special-defense" to 80,
//                    "speed" to 58
//                ),
//                description = "Often hides in water to stalk unwary prey. For swimming fast, it moves its ears to maintain balance.",
//                captureRate = 45,
//                evolutions = listOf("Blastoise"),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
//
//            val blastoise = Pokemon(
//                pokemonId = 9,
//                name = "blastoise",
//                types = listOf("water"),
//                abilities = listOf("torrent", "rain Dish"),
//                weight = 855,
//                height = 16,
//                stats = mapOf(
//                    "hp" to 79,
//                    "attack" to 83,
//                    "defense" to 100,
//                    "special-attack" to 85,
//                    "special-defense" to 105,
//                    "speed" to 78
//                ),
//                description = "It crushes its foe under its heavy body to cause fainting. In a pinch, it will withdraw inside its shell.",
//                captureRate = 45,
//                evolutions = emptyMap(),
//                generation = "generation-i",
//                countryCode = "jp"
//            )
            val jsonString = readJsonFromAssets(context, "pokemon_data.json")
            val pokemonList = parseJsonToPokemonList(jsonString)
            pokemonList.forEach {
                databaseRepository.upsert(it)
            }
            onFinished()
        }
    }
}