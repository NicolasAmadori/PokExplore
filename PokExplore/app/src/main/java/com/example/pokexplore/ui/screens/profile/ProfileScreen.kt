package com.example.pokexplore.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokexplore.ui.PokemonRoute

@Composable
fun ProfileScreen(
    navController: NavHostController,
    state: ProfileState,
    onLogOut: () -> Unit) {

    Scaffold{ contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
            modifier = Modifier.padding(contentPadding)
        ) {

        }
        Column{
            if(state.user != null) {
                Text(state.user.email)
                Text(state.user.firstName)

                Button(onClick = {
                    onLogOut()
                    navController.navigate(PokemonRoute.SignIn.route)
                }) {
                    Text("Logout")
                }
            }
        }
    }
}