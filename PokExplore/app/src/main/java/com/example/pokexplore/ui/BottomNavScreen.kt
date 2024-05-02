package com.example.pokexplore.ui

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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pokexplore.R

sealed class BottomNavItem(val route: String, val unselectedIcon: ImageVector, val selectedIcon: ImageVector, val stringId: Int) {
    data object Home : BottomNavItem(PokemonRoute.AllPokemonList.route, Icons.Outlined.Home, Icons.Filled.Home, R.string.bottom_nav_home)
    data object Catch : BottomNavItem(PokemonRoute.CatchPokemon.route, Icons.Filled.Add, Icons.Filled.AddCircle, R.string.bottom_nav_catch)
    data object Profile : BottomNavItem(PokemonRoute.Profile.route, Icons.Outlined.Person, Icons.Filled.Person, R.string.bottom_nav_profile)

    companion object {
        val items = setOf(Home,Catch, Profile)
    }
}

@Composable
fun BottomNavScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = modifier){
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                icon = { Icon( if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon, contentDescription = stringResource(item.stringId)) },
                label = { Text(stringResource(item.stringId)) },
                alwaysShowLabels = false,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}