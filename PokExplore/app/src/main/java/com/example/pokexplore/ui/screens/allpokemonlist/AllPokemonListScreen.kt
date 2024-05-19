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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.data.database.UserWithPokemons
import com.example.pokexplore.ui.PokemonRoute
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPokemonListScreen(
    navController: NavHostController,
    allPokemonListState: AllPokemonListState,
    userState: UserState,
    actions: AllPokemonListActions
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
            userState.user?.let{user ->
                items(
                    allPokemonListState.userWithPokemonsList.filter { it.user.email == user.email }
                ) { userWithPokemon ->
                    ImageCard(
                        userWithPokemon,
                        onClick = {
                            navController.navigate(PokemonRoute.PokemonDetails.buildRoute(userWithPokemon.pokemon.pokemonId))
                        },
                        onToggleFavourite = {
                            actions.toggleFavourite(userWithPokemon.user.email, userWithPokemon.pokemon.pokemonId)
                        })
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ImageCard(
    userWithPokemon: UserWithPokemons,
    onClick: () -> Unit,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

    val typeIcons = mapOf(
        "normal" to R.drawable.normal,
        "fire" to R.drawable.fire,
        "water" to R.drawable.water,
        "electric" to R.drawable.electric,
        "grass" to R.drawable.grass,
        "ice" to R.drawable.ice,
        "fighting" to R.drawable.fighting,
        "poison" to R.drawable.poison,
        "ground" to R.drawable.ground,
        "flying" to R.drawable.flying,
        "psychic" to R.drawable.psychic,
        "bug" to R.drawable.bug,
        "rock" to R.drawable.rock,
        "ghost" to R.drawable.ghost,
        "dragon" to R.drawable.dragon,
        "dark" to R.drawable.dark,
        "steel" to R.drawable.steel,
        "fairy" to R.drawable.fairy
    )

    val colorType = if (userWithPokemon.pokemon.types.size == 1) {
        userWithPokemon.pokemon.types.first()
    } else {
        userWithPokemon.pokemon.types.first { pastelColours.containsKey(it) && it != "normal" }
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(pastelColours[colorType]!!)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier= Modifier.padding(horizontal = 5.dp)
            ){
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${userWithPokemon.pokemon.pokemonId}.png"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Text(
                    text = userWithPokemon.pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "#${String.format("%03d", userWithPokemon.pokemon.pokemonId)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                userWithPokemon.pokemon.types.forEach{ type ->
                    Image(
                        painter = painterResource(typeIcons[type]!!),
                        contentDescription = stringResource(R.string.type),
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onToggleFavourite() }) {
                    Icon(
                        imageVector = if(userWithPokemon.isFavourite) {
                            Icons.Outlined.Favorite
                        }
                        else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = stringResource(R.string.favourite_label),
                        tint = if (userWithPokemon.isFavourite) Color.Red else Color.Black)
                }
            }
        }
    }
}