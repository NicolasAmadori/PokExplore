package com.example.pokexplore.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val sprite: String,
    val types: List<String>,
    val abilities: List<String>,
    val weight: Int,
    val height: Int,
    val stats: Map<String, Int>,
    val cry: String,
    val description: String,
    val captureRate: Int,
    val evolutions: List<String>,
    val generation: String,
    val countryCode: Int?
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
    val isFavourite: Boolean = false
)