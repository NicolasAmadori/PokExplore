package com.example.pokexplore.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokexplore.utilities.ArrayListConverter
import com.example.pokexplore.utilities.StringMapConverter

@Database(entities = [Pokemon::class, User::class, UserPokemon::class], version = 1)
@TypeConverters(ArrayListConverter::class, StringMapConverter::class)
abstract class PokExploreDatabase : RoomDatabase() {
    abstract fun pokemonDAO(): PokemonDAO
    abstract fun userDAO(): UserDAO
    abstract fun userPokemonDAO(): UserPokemonDAO
}