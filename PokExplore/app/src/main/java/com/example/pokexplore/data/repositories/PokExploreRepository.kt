package com.example.pokexplore.data.repositories

import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.database.PokemonDAO
import com.example.pokexplore.data.database.User
import com.example.pokexplore.data.database.UserDAO
import com.example.pokexplore.data.database.UserPokemon
import com.example.pokexplore.data.database.UserPokemonDAO
import kotlinx.coroutines.flow.Flow
import java.util.Date

class PokExploreRepository(
    private val pokexploreDAO: PokemonDAO,
    private val userDAO: UserDAO,
    private val userPokemonDAO: UserPokemonDAO
    ) {

    val pokemons: Flow<List<Pokemon>> = pokexploreDAO.getAllPokemons()

    /* Pokemon */
    suspend fun upsert(pokemon: Pokemon) = pokexploreDAO.upsert(pokemon)

    /* User */
    suspend fun insertUser(user: User) = userDAO.insert(user)

    suspend fun login(email: String, password: String) = userDAO.login(email, password)

    /* User Pokemon */
    suspend fun insertUserPokemon(userPokemon: UserPokemon) = userPokemonDAO.insertUserPokemon(userPokemon)

    suspend fun capturePokemon(email: String, pokemonId: Int) = userPokemonDAO.capturePokemon(email,pokemonId, Date())

    suspend fun toggleFavorite(email: String, pokemonId: Int) = userPokemonDAO.toggleFavorite(email, pokemonId)
}