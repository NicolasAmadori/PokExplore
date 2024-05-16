package com.example.pokexplore.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokexplore.utilities.DateConverter
import com.example.pokexplore.utilities.IntMapConverter
import com.example.pokexplore.utilities.ListConverter
import com.example.pokexplore.utilities.StringMapConverter

@Database(entities = [Pokemon::class, User::class, UserPokemon::class], version = 7)
@TypeConverters(ListConverter::class, StringMapConverter::class, IntMapConverter::class, DateConverter::class)
abstract class PokExploreDatabase : RoomDatabase() {
    abstract fun pokemonDAO(): PokemonDAO
    abstract fun userDAO(): UserDAO
    abstract fun userPokemonDAO(): UserPokemonDAO
}