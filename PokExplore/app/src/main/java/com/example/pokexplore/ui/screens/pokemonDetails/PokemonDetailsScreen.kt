package com.example.pokexplore.ui.screens.pokemonDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.database.UserWithPokemons
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.utilities.pastelColours
import java.util.Locale

@Composable
fun PokemonDetailsScreen(
    navController: NavHostController,
    state: PokemonDetailsState,
    userState: UserState,
    actions: PokemonDetailsActions,
    pokemonId: Int
) {
    userState.user?.let{user ->
        val userWithPokemon = state.pokemonList.firstOrNull { it.pokemon.pokemonId == pokemonId && it.user.email == user.email}
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            if (userWithPokemon != null) {
                HeaderSection(userWithPokemon, navController) {
                    actions.toggleFavourite(
                        userWithPokemon.user.email,
                        userWithPokemon.pokemon.pokemonId
                    )
                }
                WeightHeightSection(userWithPokemon)
                Spacer(modifier = Modifier.height(4.dp))
                DescriptionSection(userWithPokemon, userWithPokemon.pokemon.description, userWithPokemon.pokemon.abilities, userWithPokemon.pokemon.generation)
                Spacer(modifier = Modifier.height(10.dp))
                BaseStatsSection(userWithPokemon.pokemon.stats)
                Spacer(modifier = Modifier.height(16.dp))
                EvolutionSection(
                    pokemonId,
                    state.pokemonList.filter{it.user.email == user.email && userWithPokemon.pokemon.evolutions.keys.contains(it.pokemon.pokemonId)},
                    navController)
            }
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

@Composable
fun HeaderSection(
    userWithPokemon: UserWithPokemons,
    navController: NavHostController,
    onToggleFavourite: () -> Unit
) {
    val colorType = if (userWithPokemon.pokemon.types.size == 1) {
        userWithPokemon.pokemon.types.first()
    } else {
        userWithPokemon.pokemon.types.first { pastelColours.containsKey(it) && it != "normal" }
    }
    val backgroundColor = Color(pastelColours[colorType]!!)
    val painter = rememberAsyncImagePainter(
        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${userWithPokemon.pokemon.pokemonId}.png"
    )
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp),
            )
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if(navController.previousBackStackEntry?.destination?.route == PokemonRoute.CatchPokemon.route){
                        navController.navigate(PokemonRoute.AllPokemonList.route)
                    }
                    else {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_description),
                        tint = Color.White,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(if (userWithPokemon.isCaptured) R.drawable.full_pokeball else R.drawable.empty_pokeball),
                        contentDescription = stringResource(if (userWithPokemon.isCaptured) R.string.caught else R.string.not_caught),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onToggleFavourite() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (userWithPokemon.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (userWithPokemon.isFavourite) stringResource(R.string.favourite_label) else stringResource(R.string.not_favourite_label),
                            tint = if (userWithPokemon.isFavourite) Color.Red else Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = userWithPokemon.pokemon.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .background(backgroundColor, shape = CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = userWithPokemon.pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "#${userWithPokemon.pokemon.pokemonId.toString().padStart(3, '0')}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DescriptionSection(userWithPokemon: UserWithPokemons, description: String, abilities:List<String>, generation: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        userWithPokemon.pokemon.types.forEach { type ->
            TypeBadge(type = type)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
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
        val ability = abilities.joinToString(separator = ", ") { it ->
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
        Text(
            text = ability,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.generation),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = generation,
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
            text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeightHeightSection(userWithPokemon: UserWithPokemons) {
    val height = userWithPokemon.pokemon.height * 0.1
    val weight = userWithPokemon.pokemon.weight * 0.1
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
fun EvolutionSection(pokemonId: Int, userWithPokemons: List<UserWithPokemons>, navController: NavHostController) {
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
        LazyRow {
            items(userWithPokemons) { p ->
                PokemonCard(
                    pokemon = p.pokemon,
                    onClick = {
                        if(pokemonId != p.pokemon.pokemonId){
                            navController.navigate(PokemonRoute.PokemonDetails.buildRoute(p.pokemon.pokemonId))
                        }
                    })
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    val colorType = if (pokemon.types.size == 1) {
        pokemon.types.first()
    } else {
        pokemon.types.first { pastelColours.containsKey(it) && it != "normal" }
    }
    val cardColor = Color(pastelColours[colorType]!!)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.pokemonId}.png"
            )
            Image(
                painter = painter,
                contentDescription = "${pokemon.name} image",
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(8.dp)
                        clip = true
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "#${String.format("%03d", pokemon.pokemonId)}",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
