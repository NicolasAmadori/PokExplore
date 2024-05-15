package com.example.pokexplore.ui.screens.allpokemonlist


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.ui.PokemonRoute
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier
                .padding(contentPadding)
        ) {

            items(allPokemonListState.pokemonList) { pokemon ->
                ImageCard(
                    pokemon,
                    onClick = {
                        navController.navigate(PokemonRoute.PokemonDetails.buildRoute(pokemon.pokemonId))
                    })
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ImageCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
    val isFavourite = remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if(firstType != null) {
                Color(pastelColours[firstType]!!)
            }
            else {
                Color.Transparent
            }
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.pokemonId}.png"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "#${String.format("%03d", pokemon.pokemonId)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { isFavourite.value = isFavourite.value.not() }) {
                    Icon(
                        imageVector = if(isFavourite.value) {
                            Icons.Outlined.Favorite
                        }
                        else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = stringResource(R.string.favourite_label),
                        tint = if (isFavourite.value) Color.Red else Color.Black)
                }
            }
        }
    }
}