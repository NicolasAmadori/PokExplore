package com.example.pokexplore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokexplore.ui.BottomNavItem
import com.example.pokexplore.ui.BottomNavScreen
import com.example.pokexplore.ui.PokExploreViewModel
import com.example.pokexplore.ui.PokemonNavGraph
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.theme.PokExploreTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokExploreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm = koinViewModel<PokExploreViewModel>()
                    val state by vm.state.collectAsStateWithLifecycle()

                    val navController = rememberNavController()

                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            PokemonRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: PokemonRoute.AllPokemonList
                        }
                    }

                    Scaffold(
                        bottomBar = { if (shouldShowBottomNav(currentRoute)) BottomNavScreen(navController)}
                    ){ contentPadding ->
                        PokemonNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
                            startDestination = PokemonRoute.Loading.route // startDestination = if (state.pokemons.isEmpty()) PokemonRoute.Loading.route else PokemonRoute.AllPokemonList.route
                        )
                    }
                }
            }
        }
    }

    private fun shouldShowBottomNav(route: PokemonRoute): Boolean {
        return BottomNavItem.items.any { it.route == route.route }
    }
}
