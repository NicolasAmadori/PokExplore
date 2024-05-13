package com.example.pokexplore.ui.screens.pokemonDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PokemonDetailsScreen(
    navController: NavHostController,
    state: PokemonDetailsState,
    pokemonId: Int
) {
    val pokemon = state.pokemonList.firstOrNull { it.pokemonId == pokemonId}
    if(pokemon != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(pokemon.pokemonId.toString())
            Spacer(modifier = Modifier.height(10.dp))
            Text(pokemon.name)
            Spacer(modifier = Modifier.height(10.dp))
            Text(pokemon.color)
            Spacer(modifier = Modifier.height(10.dp))
            Text(pokemon.description)
            Spacer(modifier = Modifier.height(10.dp))
            Text(pokemon.stats.toString())
        }
    }

}