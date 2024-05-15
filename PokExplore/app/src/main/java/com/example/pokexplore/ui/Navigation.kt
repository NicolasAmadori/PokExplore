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
import com.example.pokexplore.ui.screens.FavouritesPokemonListScreen
import com.example.pokexplore.ui.screens.LocalPokemonListScreen
import com.example.pokexplore.ui.screens.allpokemonlist.AllPokemonListScreen
import com.example.pokexplore.ui.screens.allpokemonlist.AllPokemonListViewModel
import com.example.pokexplore.ui.screens.gpsMandatory.GpsMandatoryScreen
import com.example.pokexplore.ui.screens.gpsMandatory.GpsMandatoryViewModelViewModel
import com.example.pokexplore.ui.screens.loading.LoadingScreen
import com.example.pokexplore.ui.screens.loading.LoadingViewModel
import com.example.pokexplore.ui.screens.pokemonDetails.PokemonDetailsScreen
import com.example.pokexplore.ui.screens.pokemonDetails.PokemonDetailsViewModel
import com.example.pokexplore.ui.screens.profile.ProfileScreen
import com.example.pokexplore.ui.screens.profile.ProfileViewModel
import com.example.pokexplore.ui.screens.settings.SettingsScreen
import com.example.pokexplore.ui.screens.signin.SignInScreen
import com.example.pokexplore.ui.screens.signin.SignInViewModel
import com.example.pokexplore.ui.screens.signup.SignUpScreen
import com.example.pokexplore.ui.screens.signup.SignUpViewModel
import com.example.pokexplore.ui.screens.theme.ThemeScreen
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
    data object Profile : PokemonRoute("profile", "Profile")
    data object LocalPokemonList : PokemonRoute("localPokemonList", "Local Pokemon List")
    data object FavouritesPokemonList : PokemonRoute("favouritesPokemonList", "Favourites Pokemon List")
    data object SignIn : PokemonRoute("signIn", "Sign In")
    data object SignUp: PokemonRoute("signUp", "Sign Up")
    data object Loading: PokemonRoute("loading", "Loading")
    data object Theme: PokemonRoute("theme", "Theme")
    data object GpsMandatory: PokemonRoute("gpsMandatory", "Gps Mandatory")
    companion object {
        val routes = setOf(AllPokemonList, PokemonDetails, CatchPokemon, Settings, Profile, LocalPokemonList, FavouritesPokemonList, SignIn, SignUp, Loading, Theme, GpsMandatory)
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
                val allPokemonListVm = koinViewModel<AllPokemonListViewModel>()
                val state by allPokemonListVm.state.collectAsStateWithLifecycle()
                AllPokemonListScreen(navController, state, allPokemonListVm)
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
                val pokemonDetailsVm = koinViewModel<PokemonDetailsViewModel>()
                val state by pokemonDetailsVm.state.collectAsStateWithLifecycle()
                val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: -1
                if(pokemonId != -1) {
                    PokemonDetailsScreen(navController, state, pokemonId)
                }
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
                val loadingVm = koinViewModel<LoadingViewModel>()
                val state by loadingVm.state.collectAsStateWithLifecycle()
                LoadingScreen(navController, state, loadingVm.actions)
            }
        }
        with(PokemonRoute.GpsMandatory) {
            composable(route) {
                val gpsMandatoryVm = koinViewModel<GpsMandatoryViewModelViewModel>()
                val state = gpsMandatoryVm.state
                GpsMandatoryScreen(navController, state, gpsMandatoryVm::setCountryCode)
            }
        }
    }
}
