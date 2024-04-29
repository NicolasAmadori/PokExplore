package com.example.pokexplore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokexplore.ui.PokExploreViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonDetailsScreen(pokemonId: Int) {
    val vm = koinViewModel<PokExploreViewModel>()
    val state by vm.state.collectAsStateWithLifecycle()
    val pokemon = state.pokemons.firstOrNull { it.pokemonId == pokemonId }
    if (pokemon != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(pokemon.pokemonId.toString())
            Spacer(modifier = Modifier.height(20.dp))
            Text(pokemon.name)
            Spacer(modifier = Modifier.height(20.dp))
            Text(pokemon.color)
            Spacer(modifier = Modifier.height(20.dp))
            Text(pokemon.description)
        }
    }
}