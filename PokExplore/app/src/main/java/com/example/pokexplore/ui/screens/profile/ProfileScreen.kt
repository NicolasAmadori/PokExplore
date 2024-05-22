package com.example.pokexplore.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.ui.PieChart
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.allpokemonlist.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    state: UserState,
    pokemonState: PokemonState,
    actions: ProfileActions) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.bottom_nav_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        actions.logOut()
                        navController.navigate(PokemonRoute.SignIn.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = stringResource(R.string.logout_description)
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(PokemonRoute.Settings.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.user?.let { user ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(8),
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(100.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "${user.firstName} ${user.lastName}",
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.labelMedium,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        PieChart(
                            data = listOf(
                                Triple(stringResource(R.string.not_catched), pokemonState.userWithPokemonsList.count { !it.isCaptured }, MaterialTheme.colorScheme.primaryContainer),
                                Triple(stringResource(R.string.catched), pokemonState.userWithPokemonsList.count { it.isCaptured }, MaterialTheme.colorScheme.onPrimaryContainer)
                            ),
                            radiusOuter = 50.dp,
                            chartBarWidth = 20.dp,
                            animDuration = 300,
                        )
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        Text(
                            text = stringResource(R.string.caught_pokemons_label),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge
                            )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                        ) {
                            items(pokemonState.userWithPokemonsList.filter { it.user.email == state.user.email && it.isCaptured }) { userWithPokemon ->
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
        }
    }
}