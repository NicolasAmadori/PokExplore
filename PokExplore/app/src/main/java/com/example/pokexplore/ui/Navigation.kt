package com.example.pokexplore.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokexplore.ui.screens.AddPokemonScreen
import com.example.pokexplore.ui.screens.PokemonDetailsScreen
import com.example.pokexplore.ui.screens.PokemonListScreen
import com.example.pokexplore.ui.screens.SettingsScreen

sealed class PokemonRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : PokemonRoute("pokemonList", "Pokemon List")
    data object PokemonDetails : PokemonRoute(
        "pokemon/{pokemonId}",
        "Pokemon Details",
        listOf(navArgument("pokemonId") { type = NavType.StringType })
    ) {
        fun buildRoute(pokemonId: String) = "pokemon/$pokemonId"
    }
    data object AddPokemon : PokemonRoute("addPokemon", "Add Pokemon")
    data object Settings : PokemonRoute("settings", "Settings")

    companion object {
        val routes = setOf(Home, PokemonDetails, AddPokemon, Settings)
    }
}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PokemonRoute.Home.route,
        modifier = modifier
    ) {
        with(PokemonRoute.Home) {
            composable(route) {
                PokemonListScreen(navController)
            }
        }
        with(PokemonRoute.PokemonDetails) {
            composable(route, arguments) { backStackEntry ->
                PokemonDetailsScreen(backStackEntry.arguments?.getString("pokemonId") ?: "")
            }
        }
        with(PokemonRoute.AddPokemon) {
            composable(route) {
                AddPokemonScreen(navController)
            }
        }
        with(PokemonRoute.Settings) {
            composable(route) {
                SettingsScreen()
            }
        }
    }
}
