package com.example.pokexplore.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.ui.screens.CatchPokemonScreen
import com.example.pokexplore.ui.screens.PokemonDetailsScreen
import com.example.pokexplore.ui.screens.AllPokemonListScreen
import com.example.pokexplore.ui.screens.FavouritesPokemonListScreen
import com.example.pokexplore.ui.screens.LoadingScreen
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
        listOf(navArgument("pokemonId") { type = NavType.IntType })
    ) {
        fun buildRoute(pokemonId: Int) = "pokemon/$pokemonId"
    }
    data object CatchPokemon : PokemonRoute("catchPokemon", "Catch Pokemon")
    data object Settings : PokemonRoute("settings", "Settings")
    data object Profile : PokemonRoute("profile", "Profile")//TODO: passare id della persona
    data object LocalPokemonList : PokemonRoute("localPokemonList", "Local Pokemon List")
    data object FavouritesPokemonList : PokemonRoute("favouritesPokemonList", "Favourites Pokemon List")
    data object SignIn : PokemonRoute("signIn", "Sign In")
    data object SignUp: PokemonRoute("signUp", "Sign Up")
    data object Loading: PokemonRoute("loading", "Loading")
    companion object {
        val routes = setOf(AllPokemonList, PokemonDetails, CatchPokemon, Settings, Profile, LocalPokemonList, FavouritesPokemonList, SignIn, SignUp, Loading)
    }
}

@Composable
fun PokemonNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        with(PokemonRoute.AllPokemonList) {
            composable(route) {
                AllPokemonListScreen(navController)
            }
        }
        with(PokemonRoute.PokemonDetails) {
            composable(route, arguments) { backStackEntry ->
                PokemonDetailsScreen(backStackEntry.arguments?.getInt("pokemonId") ?: -1)
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
        with(PokemonRoute.Loading) {
            composable(route) {
                LoadingScreen(navController)
            }
        }
    }
}
