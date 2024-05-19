package com.example.pokexplore.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Pokemon(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val types: List<String>,
    val abilities: List<String>,
    val weight: Int,
    val height: Int,
    val stats: Map<String, Int>,
    val description: String,
    val captureRate: Int,
    val evolutions: Map<Int, String>,
    val generation: String,
    val countryCode: String?
)

@Entity
data class User(
    @PrimaryKey val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: Int?,
    val profilePicUrl: String?
)

@Entity(primaryKeys = ["email", "pokemonId"])
data class UserPokemon(
    val email: String,
    val pokemonId: Int,
    val isCaptured: Boolean = false,
    val isFavourite: Boolean = false,
    val captureDate: Date? = null
)

data class UserWithPokemons(
    @Embedded val user: User,
    @Embedded val pokemon: Pokemon,
    val isCaptured: Boolean,
    val isFavourite: Boolean,
    val captureDate: Date?
)