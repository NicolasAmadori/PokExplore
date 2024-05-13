package com.example.pokexplore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokexplore.ui.screens.CatchPokemonScreen
import com.example.pokexplore.ui.screens.PokemonDetailsScreen
import com.example.pokexplore.ui.screens.AllPokemonListScreen
import com.example.pokexplore.ui.screens.theme.ThemeScreen
import com.example.pokexplore.ui.screens.FavouritesPokemonListScreen
import com.example.pokexplore.ui.screens.LoadingScreen
import com.example.pokexplore.ui.screens.LocalPokemonListScreen
import com.example.pokexplore.ui.screens.profile.ProfileScreen
import com.example.pokexplore.ui.screens.settings.SettingsScreen
import com.example.pokexplore.ui.screens.signin.SignInScreen
import com.example.pokexplore.ui.screens.profile.ProfileViewModel
import com.example.pokexplore.ui.screens.signin.SignInViewModel
import com.example.pokexplore.ui.screens.signup.SignUpScreen
import com.example.pokexplore.ui.screens.signup.SignUpViewModel
import com.example.pokexplore.ui.screens.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

sealed class PokemonRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList(),
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
    data object Theme: PokemonRoute("theme", "Theme")
    companion object {
        val routes = setOf(AllPokemonList, PokemonDetails, CatchPokemon, Settings, Profile, LocalPokemonList, FavouritesPokemonList, SignIn, SignUp, Loading, Theme)
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
        with(PokemonRoute.Theme) {
            composable(route) {
                val themeVm = koinViewModel<ThemeViewModel>()
                val state by themeVm.state.collectAsStateWithLifecycle()
                ThemeScreen(navController, state, themeVm::changeTheme )
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
                SettingsScreen(navController)
            }
        }
        with(PokemonRoute.Profile) {
            composable(route) {
                val profileVm = koinViewModel<ProfileViewModel>()
                ProfileScreen(navController, profileVm.state, profileVm::logOut)
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
                val signInVm = koinViewModel<SignInViewModel>()
                SignInScreen(navController, signInVm)
            }
        }
        with(PokemonRoute.SignUp) {
            composable(route) {
                val signUpVm = koinViewModel<SignUpViewModel>()
                val dbVm = koinViewModel<PokExploreViewModel>()
                SignUpScreen(navController) {
                    signUpVm.setUser(it)
                    dbVm.actions.addUser(it)
                }
            }
        }
        with(PokemonRoute.Loading) {
            composable(route) {
                LoadingScreen(navController)
            }
        }
    }
}
