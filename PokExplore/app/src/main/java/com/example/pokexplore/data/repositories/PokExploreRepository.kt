package com.example.pokexplore.data.repositories

import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.database.PokemonDAO
import com.example.pokexplore.data.database.UserDAO
import com.example.pokexplore.data.database.UserPokemonDAO
import kotlinx.coroutines.flow.Flow

class PokExploreRepository(
    private val pokexploreDAO: PokemonDAO,
    private val userDAO: UserDAO,
    private val userPokemonDAO: UserPokemonDAO
    ) {

    val pokemons: Flow<List<Pokemon>> = pokexploreDAO.getAllPokemons()

    suspend fun upsert(pokemon: Pokemon) = pokexploreDAO.upsert(pokemon)
}