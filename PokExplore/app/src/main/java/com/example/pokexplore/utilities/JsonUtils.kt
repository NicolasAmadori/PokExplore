package com.example.pokexplore.utilities

import android.content.Context
import com.example.pokexplore.data.database.Pokemon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun readJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

fun parseJsonToPokemonList(jsonString: String): List<Pokemon> {
    val gson = Gson()
    return gson.fromJson(jsonString, object : TypeToken<List<Pokemon>>() {}.type)
}