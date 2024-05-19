package com.example.pokexplore.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.Date

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
    suspend fun upsert(pokemons: List<Pokemon>)

//    @Query("UPDATE UserPokemon SET isCaptured = 1 WHERE user = :user AND pokemon = :pokemon")
//    suspend fun capture(user: User, pokemon: Pokemon)
//
//    @Query("UPDATE UserPokemon SET isFavourite = CASE WHEN :state THEN 1 ELSE 0 END WHERE user = user AND pokemon = pokemon")
//    suspend fun toggleFavourite(user: User, pokemon: Pokemon, state: Boolean)
}

@Dao
interface UserPokemonDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserPokemon(userPokemonList: List<UserPokemon>)

    @Query("UPDATE UserPokemon SET isCaptured = 1, captureDate = :captureDate WHERE email = :email AND pokemonId = :pokemonId AND isCaptured = 0")
    suspend fun capturePokemon(email: String, pokemonId: Int, captureDate: Date)

    @Query("UPDATE UserPokemon SET isFavourite = NOT isFavourite WHERE email = :email AND pokemonId = :pokemonId")
    suspend fun toggleFavorite(email: String, pokemonId: Int)
}

@Dao
interface UserDAO {

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}

@Dao
interface UserWithPokemonsDao {
    @Query("""
        select User.*, Pokemon.*, UserPokemon.isFavourite, UserPokemon.isCaptured, UserPokemon.captureDate
        from User inner join UserPokemon on user.email = UserPokemon.email 
        inner join Pokemon on Pokemon.pokemonId = UserPokemon.pokemonId
        """)
    fun getUserPokemons(): Flow<List<UserWithPokemons>>
}
