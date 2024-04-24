package com.example.pokexplore.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokexplore.ui.screens.CatchPokemonScreen
import com.example.pokexplore.ui.screens.PokemonDetailsScreen
import com.example.pokexplore.ui.screens.AllPokemonListScreen
import com.example.pokexplore.ui.screens.FavouritesPokemonListScreen
import com.example.pokexplore.ui.screens.LocalPokemonListScreen
import com.example.pokexplore.ui.screens.ProfileScreen
import com.example.pokexplore.ui.screens.SettingsScreen
import com.example.pokexplore.ui.screens.SignInScreen
import com.example.pokexplore.ui.screens.SignUpScreen

sealed class PokemonRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object AllPokemonList : PokemonRoute("allPokemonList", "Pokemon List")
    data object PokemonDetails : PokemonRoute(
        "pokemon/{pokemonId}",
        "Pokemon Details",
        listOf(navArgument("pokemonId") { type = NavType.StringType })
    ) {
        fun buildRoute(pokemonId: String) = "pokemon/$pokemonId"
    }
    data object CatchPokemon : PokemonRoute("catchPokemon", "Catch Pokemon")
    data object Settings : PokemonRoute("settings", "Settings")
    data object  Profile : PokemonRoute("profile", "Profile")//TODO: passare id della persona
    data object LocalPokemonList : PokemonRoute("localPokemonList", "Local Pokemon List")
    data object FavouritesPokemonList : PokemonRoute("favouritesPokemonList", "Favourites Pokemon List")
    data object SignIn : PokemonRoute("signIn", "Sign In")
    data object SignUp: PokemonRoute("signUp", "Sign Up")
    companion object {
        val routes = setOf(AllPokemonList, PokemonDetails, CatchPokemon, Settings, Profile, LocalPokemonList, FavouritesPokemonList, SignIn, SignUp)
    }
}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PokemonRoute.AllPokemonList.route,
        modifier = modifier
    ) {
        with(PokemonRoute.AllPokemonList) {
            composable(route) {
                AllPokemonListScreen(navController)
            }
        }
        with(PokemonRoute.PokemonDetails) {
            composable(route, arguments) { backStackEntry ->
                PokemonDetailsScreen(backStackEntry.arguments?.getString("pokemonId") ?: "")
            }
        }
        with(PokemonRoute.CatchPokemon) {
            composable(route) {
                CatchPokemonScreen(navController)
            }
        }
        with(PokemonRoute.Settings) {
            composable(route) {
                SettingsScreen()
            }
        }
        with(PokemonRoute.Profile) {
            composable(route) {
                ProfileScreen()//TODO: passare id profilo
            }
        }
        with(PokemonRoute.LocalPokemonList) {
            composable(route) {
                LocalPokemonListScreen()
            }
        }
        with(PokemonRoute.FavouritesPokemonList) {
            composable(route) {
                FavouritesPokemonListScreen()
            }
        }
        with(PokemonRoute.SignIn) {
            composable(route) {
                SignInScreen()
            }
        }
        with(PokemonRoute.SignUp) {
            composable(route) {
                SignUpScreen()
            }
        }

    }
}
