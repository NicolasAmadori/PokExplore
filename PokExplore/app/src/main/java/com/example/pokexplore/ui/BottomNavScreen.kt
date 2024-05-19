package com.example.pokexplore.ui

import android.Manifest
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pokexplore.R
import com.example.pokexplore.utilities.rememberPermission

sealed class BottomNavItem(val route: String, val unselectedIcon: ImageVector, val selectedIcon: ImageVector, val stringId: Int) {
    data object Home : BottomNavItem(PokemonRoute.AllPokemonList.route, Icons.Outlined.Home, Icons.Filled.Home, R.string.bottom_nav_home)
    data object Catch : BottomNavItem(PokemonRoute.CatchPokemon.route, Icons.Filled.Add, Icons.Filled.AddCircle, R.string.bottom_nav_catch)
    data object Profile : BottomNavItem(PokemonRoute.Profile.route, Icons.Outlined.Person, Icons.Filled.Person, R.string.bottom_nav_profile)

    companion object {
        val items = setOf(Home, Catch, Profile)
    }
}

@Composable
fun BottomNavScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val ctx = LocalContext.current
    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            navController.navigate(PokemonRoute.CatchPokemon.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestPermission() =
        if (cameraPermission.status.isGranted) {
            navController.navigate(PokemonRoute.CatchPokemon.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            cameraPermission.launchPermissionRequest()
        }

    NavigationBar(modifier = modifier){
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                icon = { Icon( if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon, contentDescription = stringResource(item.stringId)) },
                label = { Text(stringResource(item.stringId)) },
                selected = currentRoute == item.route,
                onClick = {
                    if(currentRoute != item.route) {
                        if(item.route != PokemonRoute.CatchPokemon.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        else {
                            requestPermission()
                        }
                    }
                }
            )
        }
    }
}