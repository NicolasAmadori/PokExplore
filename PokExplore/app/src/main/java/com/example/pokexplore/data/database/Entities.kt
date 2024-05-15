package com.example.pokexplore.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val isFavourite: Boolean = false
)