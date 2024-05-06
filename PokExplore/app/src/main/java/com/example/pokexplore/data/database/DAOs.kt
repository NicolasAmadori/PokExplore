package com.example.pokexplore.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Insert

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM Pokemon")
    fun getAllPokemons(): Flow<List<Pokemon>>

    //@Query("SELECT Pokemon.* FROM Pokemon INNER JOIN UserPokemon ON Pokemon.pokemonId = UserPokemon.pokemon.pokemonId WHERE UserPokemon.isCaptured = 1 AND UserPokemon.email = :user")
    //fun getCapturedPokemons(user: User): Flow<List<Pokemon>>

    //@Query("SELECT Pokemon.* FROM Pokemon INNER JOIN UserPokemon ON Pokemon.pokemonId = UserPokemon.pokemonId WHERE UserPokemon.isFavourite = 1 AND UserPokemon.user = :user")
    //fun getFavouritePokemons(user: User): Flow<List<Pokemon>>

//    @Query("SELECT * FROM Pokemon  WHERE Pokemon.countryCode = :countryCode")
//    fun getLocalPokemons(countryCode: Int): Flow<List<Pokemon>>

    @Upsert
    suspend fun upsert(pokemon: Pokemon)

//    @Query("UPDATE UserPokemon SET isCaptured = 1 WHERE user = :user AND pokemon = :pokemon")
//    suspend fun capture(user: User, pokemon: Pokemon)
//
//    @Query("UPDATE UserPokemon SET isFavourite = CASE WHEN :state THEN 1 ELSE 0 END WHERE user = user AND pokemon = pokemon")
//    suspend fun toggleFavourite(user: User, pokemon: Pokemon, state: Boolean)
}

@Dao
interface UserPokemonDAO {

    //@Query("UPDATE UserPokemon SET isCaptured = 1 WHERE user = :user AND pokemon = :pokemon")
    //suspend fun addUserAssociations(user: User)
}

@Dao
interface UserDAO {

//    @Query("SELECT * FROM User  WHERE email = :email AND password = :password")
//    fun checkCredentials(email: String, password: String): Flow<List<User>>

    @Insert
    suspend fun insert(user: User)
}
