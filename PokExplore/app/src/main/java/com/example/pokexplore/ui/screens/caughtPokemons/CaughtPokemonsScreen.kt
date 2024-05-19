package com.example.pokexplore.ui.screens.caughtPokemons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.allpokemonlist.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaughtPokemonsScreen(
    navController: NavHostController,
    allPokemonListState: CaughtPokemonState,
    userState: UserState,
    actions: CaughtPokemonsActions
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.caught_pokemons_label),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ){ contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
            modifier = Modifier
                .padding(contentPadding)
        ) {
            if(userState.user != null) {
                items(allPokemonListState.userWithPokemonsList.filter { it.user.email == userState.user.email && it.isCaptured }) { userWithPokemon ->
                    PokemonCard(
                        userWithPokemon,
                        onClick = {
                            navController.navigate(PokemonRoute.PokemonDetails.buildRoute(userWithPokemon.pokemon.pokemonId))
                        },
                        onToggleFavourite = {
                            actions.toggleFavourite(userWithPokemon.user.email, userWithPokemon.pokemon.pokemonId)
                        }
                    )
                }
            }
        }
    }
}