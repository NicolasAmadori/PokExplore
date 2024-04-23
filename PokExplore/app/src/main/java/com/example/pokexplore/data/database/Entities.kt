package com.example.pokexplore.data.database

import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    @PrimaryKey val pokemonId: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val sprite: String,
    @ColumnInfo val types: ArrayList<String>,
    @ColumnInfo val abilities: ArrayList<String>,
    @ColumnInfo val weight: Int,
    @ColumnInfo val height: Int,
    @ColumnInfo val stats: Map<String, Int>,
    @ColumnInfo val cry: String,
    @ColumnInfo val color: String,
    @ColumnInfo val description: String,
    @ColumnInfo val captureRate: Int,
    @ColumnInfo val evolutions: ArrayList<String>,
    @ColumnInfo val generation: String,
    @ColumnInfo val countryCode: Int
)

@Entity
data class User(
    @PrimaryKey val email: String,
    @ColumnInfo val password: String,
    @ColumnInfo val firstName: String,
    @ColumnInfo val lastName: String,
    @ColumnInfo val phone: Int,
    @ColumnInfo val profilePicUrl: String
)

@Entity(primaryKeys = ["email", "pokemonId"])
data class UserPokemon(
    val email: String,
    val pokemonId: Int,
    @ColumnInfo val isCaptured: Boolean = false,
    @ColumnInfo val isFavourite: Boolean = false
)