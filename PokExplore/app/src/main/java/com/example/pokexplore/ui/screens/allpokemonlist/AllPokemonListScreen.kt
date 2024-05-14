package com.example.pokexplore.ui.screens.allpokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.ui.PokemonRoute

@Composable
fun AllPokemonListScreen(
    navController: NavHostController,
    allPokemonListState: AllPokemonListState
) {
    Scaffold{ contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
            modifier = Modifier.padding(contentPadding)
        ) {

            items(allPokemonListState.pokemonList) { pokemon ->
                PokemonCard(
                    pokemon,
                    onClick = {
                        navController.navigate(PokemonRoute.PokemonDetails.buildRoute(pokemon.pokemonId))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    val colours = mapOf(
        "normal" to 0xFFA8A77A,
        "fire" to 0xFFEE8130,
        "water" to 0xFF6390F0,
        "electric" to 0xFFF7D02C,
        "grass" to 0xFF7AC74C,
        "ice" to 0xFF96D9D6,
        "fighting" to 0xFFC22E28,
        "poison" to 0xFFA33EA1,
        "ground" to 0xFFE2BF65,
        "flying" to 0xFFA98FF3,
        "psychic" to 0xFFF95587,
        "bug" to 0xFFA6B91A,
        "rock" to 0xFFB6A136,
        "ghost" to 0xFF735797,
        "dragon" to 0xFF6F35FC,
        "dark" to 0xFF705746,
        "steel" to 0xFFB7B7CE,
        "fairy" to 0xFFD685AD
    )
    val pastelColours = mapOf(
        "normal" to 0xFFBAB5A5,
        "fire" to 0xFFF8A689,
        "water" to 0xFF8CB8F2,
        "electric" to 0xFFFFE580,
        "grass" to 0xFFB8E6B8,
        "ice" to 0xFFBFECEB,
        "fighting" to 0xFFE69D99,
        "poison" to 0xFFE29EDC,
        "ground" to 0xFFECCC8C,
        "flying" to 0xFFCDC9F0,
        "psychic" to 0xFFFEC1D5,
        "bug" to 0xFFCED6A3,
        "rock" to 0xFFD7D0B2,
        "ghost" to 0xFFB5ABC9,
        "dragon" to 0xFFC79CF6,
        "dark" to 0xFFC8C3BD,
        "steel" to 0xFFDCDCE9,
        "fairy" to 0xFFE9BCC9
    )

    val firstType = pokemon.types.firstOrNull { pastelColours.containsKey(it) }

    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if(firstType != null) {
                Color(pastelColours[firstType]!!)
                }
                else {
                    Color.Transparent
                }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                Icons.Filled.Image,
                "Travel picture",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = pokemon.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "#${String.format("%03d", pokemon.pokemonId)}",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

