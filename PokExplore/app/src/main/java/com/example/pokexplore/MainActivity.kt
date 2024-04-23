package com.example.pokexplore

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokexplore.ui.PokemonNavGraph
import com.example.pokexplore.ui.PokemonRoute
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pokexplore.data.remote.EvolutionChain
import com.example.pokexplore.data.remote.PokeApiDataSource
import com.example.pokexplore.data.remote.Pokemon
import com.example.pokexplore.data.remote.PokemonInfo
import com.example.pokexplore.data.remote.PokemonSpecies
import com.example.pokexplore.ui.theme.PokExploreTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokExploreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            PokemonRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: PokemonRoute.Home
                        }
                    }

                    val snackbarHostState = remember { SnackbarHostState() }

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        //topBar = { AppBar(navController, currentRoute) }
                    ) { contentPadding ->
                        PokemonNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )

                        Column(
                            modifier = Modifier.padding(contentPadding).padding(16.dp)
                        ) {
                            var text by remember { mutableStateOf("") }
                            var info by remember { mutableStateOf<PokemonInfo?>(null) }
                            var species by remember { mutableStateOf<PokemonSpecies?>(null) }
                            var pokemon by remember { mutableStateOf<Pokemon?>(null) }
                            var evolutionChain by remember { mutableStateOf<EvolutionChain?>(null) }

                            val ctx = LocalContext.current
                            fun isOnline(): Boolean {
                                val connectivityManager = ctx
                                    .applicationContext
                                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                val capabilities =
                                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                                return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                                        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                            }
                            fun openWirelessSettings() {
                                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                if (intent.resolveActivity(applicationContext.packageManager) != null) {
                                    applicationContext.startActivity(intent)
                                }
                            }

                            val pokeApiDataSource = koinInject<PokeApiDataSource>()

                            val coroutineScope = rememberCoroutineScope()
                            fun searchPlaces() = coroutineScope.launch {
                                if (isOnline()) {
                                    info = pokeApiDataSource.getPokemonByName(text)
                                    species = pokeApiDataSource.getPokemonSpeciesByName(text)
                                    evolutionChain = pokeApiDataSource.getEvolutionChainByUrl(species!!.evolutionChain.url)
                                    pokemon = Pokemon(info = info!!, species = species!!, evolutionChain = evolutionChain!!)

                                } else {
                                    val res = snackbarHostState.showSnackbar(
                                        message = "No Internet connectivity",
                                        actionLabel = "Go to Settings",
                                        duration = SnackbarDuration.Long
                                    )
                                    if (res == SnackbarResult.ActionPerformed) {
                                        openWirelessSettings()
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = text,
                                onValueChange = { text = it },
                                trailingIcon = {
                                    IconButton(onClick = ::searchPlaces) {
                                        Icon(Icons.Outlined.Search, "Search")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.size(16.dp))
                            Text("Result: ${when {
                                pokemon != null -> "${pokemon?.getAllInfoAsString()}"
                                else -> "-"
                            }}")
                        }
                    }
                }
            }
        }
    }
}
