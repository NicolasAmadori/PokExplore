package com.example.pokexplore.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*Pokemon info classes*/

@Serializable
data class TypeSlot(
    @SerialName("type")
    val type: Type
)

@Serializable
data class Type(
    @SerialName("name")
    val name: String
)

@Serializable
data class AbilitySlot(
    @SerialName("ability")
    val ability: Ability
)

@Serializable
data class Ability(
    @SerialName("name")
    val name: String
)

@Serializable
data class Stat(
    @SerialName("name")
    val name: String
)

@Serializable
data class StatValue(
    @SerialName("base_stat")
    val value: Int,
    @SerialName("stat")
    val stat: Stat
)

@Serializable
data class Cry(
    @SerialName("legacy")
    val cry: String
)

@Serializable
data class Sprite(
    @SerialName("front_default")
    val front: String
)

@Serializable
data class PokemonInfo(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("sprites")
    val sprite: Sprite,
    @SerialName("types")
    val types: List<TypeSlot>,
    @SerialName("abilities")
    val abilities: List<AbilitySlot>,
    @SerialName("weight")
    val weight: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("stats")
    val stats: List<StatValue>,
    @SerialName("cries")
    val cry: Cry
)

/*Pokemon species classes*/

@Serializable
data class FlavourText(
    @SerialName("flavor_text")
    val flavorText: String,
    @SerialName("language")
    val language: Language
)

@Serializable
data class Language(
    @SerialName("name")
    val name: String
)

@Serializable
data class EvolutionChainUrl(
    @SerialName("url")
    val url: String
)

@Serializable
data class Generation(
    @SerialName("name")
    val name: String
)

@Serializable
data class PokemonSpecies(
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavourText>,
    @SerialName("capture_rate")
    val captureRate: Int,
    @SerialName("evolution_chain")
    val evolutionChain: EvolutionChainUrl,
    @SerialName("generation")
    val generation: Generation
)

/*Evolution chain classes*/

@Serializable
data class Species(
    @SerialName("name")
    val name: String,
)

@Serializable
data class Chain(
    @SerialName("evolves_to")
    val evolvesTo: List<Chain>,
    @SerialName("species")
    val species: Species
)

@Serializable
data class EvolutionChain(
    @SerialName("chain")
    val chain: Chain
)

class PokeApiDataSource(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://pokeapi.co/api/v2"
    private val pokemonEndpoint = "$baseUrl/pokemon/"
    private val pokemonSpeciesEndpoint = "$baseUrl/pokemon-species/"
    private val evolutionChainEndpoint = "$baseUrl/evolution-chain/"

    suspend fun getPokemonInfoById(pokemonId: Int): PokemonInfo {
        return httpClient.get("$pokemonEndpoint$pokemonId").body()
    }

    suspend fun getPokemonInfoByName(pokemonName: String): PokemonInfo {
        return httpClient.get("$pokemonEndpoint$pokemonName").body()
    }

    suspend fun getPokemonInfoByUrl(pokemonUrl: String): PokemonInfo {
        return httpClient.get(pokemonUrl).body()
    }

    suspend fun getPokemonSpeciesById(pokemonSpeciesId: Int): PokemonSpecies {
        return httpClient.get("$pokemonSpeciesEndpoint$pokemonSpeciesId").body()
    }

    suspend fun getPokemonSpeciesByName(pokemonSpeciesName: String): PokemonSpecies {
        return httpClient.get("$pokemonSpeciesEndpoint$pokemonSpeciesName").body()
    }

    suspend fun getPokemonSpeciesByUrl(pokemonSpeciesUrl: String): PokemonSpecies {
        return httpClient.get(pokemonSpeciesUrl).body()
    }

    suspend fun getEvolutionChainById(evolutionChainId: Int): EvolutionChain {
        return httpClient.get("$evolutionChainEndpoint$evolutionChainId").body()
    }

    suspend fun getEvolutionChainByUrl(evolutionChainUrl: String): EvolutionChain {
        return httpClient.get(evolutionChainUrl).body()
    }
}
