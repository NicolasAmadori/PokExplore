package com.example.pokexplore.ui.screens.pokemonDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.allpokemonlist.ImageCard

@Composable
fun PokemonDetailsScreen(
    navController: NavHostController,
    state: PokemonDetailsState,
    pokemonId: Int
) {
    val pokemon = state.pokemonList.firstOrNull { it.pokemonId == pokemonId }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        if (pokemon != null) {
            HeaderSection(pokemon, navController)
            Spacer(modifier = Modifier.height(16.dp))
            WeightHeightSection(pokemon)
            Spacer(modifier = Modifier.height(16.dp))
            DescriptionSection(pokemon, pokemon.description, pokemon.abilities)
            Spacer(modifier = Modifier.height(16.dp))
            BaseStatsSection(pokemon.stats)
            Spacer(modifier = Modifier.height(16.dp))
            EvolutionSection(pokemon, navController)
        }
    }
}

val statsMap = mapOf(
    "hp" to R.string.hp_label,
    "attack" to R.string.atk_label,
    "defense" to R.string.def_label,
    "special-attack" to R.string.atksp_label,
    "special-defense" to R.string.defsp_label,
    "speed" to R.string.spd_label
)

val statColors = mapOf(
    "hp" to Color(0xFFFF0000), // Rosso
    "attack" to Color(0xFFFFA500), // Arancione
    "defense" to Color(0xFF32CD32), // Verde Lime
    "special-attack" to Color(0xFF1E90FF), // Blu
    "special-defense" to Color(0xFF9932CC), // Viola
    "speed" to Color(0xFFFFFF00) // Giallo
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

@Composable
fun HeaderSection(pokemon: Pokemon, navController: NavHostController) {
    val backgroundColor = Color(pastelColours[pokemon.types[0]]!!)
    val painter = rememberAsyncImagePainter(
        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.pokemonId}.png"
    )
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_description),
                        tint = Color.White,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        //imageVector = if (pokemon.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        //contentDescription = if (pokemon.isFavorite) stringResource(R.string.favorite) else stringResource(R.string.not_favorite),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.favourite_label),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        //contentDescription = stringResource(R.string.caught),
                        contentDescription = stringResource(R.string.theme_dark),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .background(backgroundColor, shape = CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pokemon.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DescriptionSection(pokemon: Pokemon, description: String, abilities:List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        pokemon.types.forEach { type ->
            TypeBadge(type = type)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.pokedex_description),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = description,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = stringResource(R.string.abilities),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val ability = abilities.joinToString(separator = ",")
        Text(
            text = ability,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun TypeBadge(type: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                Color(pastelColours[type]!!),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = type,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeightHeightSection(pokemon: Pokemon) {
    val height = pokemon.height * 0.1
    val weight = pokemon.weight * 0.1
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = String.format("%.1f KG", weight))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = String.format("%.1f M", height))
            }
        }
    }
}

@Composable
fun BaseStatsBarChart(stats: Map<String, Int>, maxStats: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        stats.forEach { (statName, statValue) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(statsMap[statName] ?: 2) , modifier = Modifier.width(100.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(((statValue.toFloat() / maxStats) * 200).dp)
                            .background(
                                statColors[statName] ?: Color.White,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = statValue.toString(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BaseStatsSection(stats: Map<String, Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.base_stats), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        BaseStatsBarChart(stats = stats, maxStats = 300, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun EvolutionSection(pokemon: Pokemon, navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.evolutions),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        val evolution = pokemon.evolutions.keys.filter { valu -> valu != pokemon.pokemonId  }.toList()
        LazyRow {
            items(evolution) { Id ->
                PokemonCard(
                    pokemonName = pokemon.evolutions[Id] ?: "h",
                    pokemonId = Id,
                    onClick = {
                        navController.navigate(PokemonRoute.PokemonDetails.buildRoute(Id))
                    })
            }
        }
    }
}

@Composable
fun PokemonCard(pokemonName: String, pokemonId: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        //elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Inserisci qui l'immagine del Pokémon
            Text(
                text = pokemonName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // Altre informazioni del Pokémon
        }
    }
}
