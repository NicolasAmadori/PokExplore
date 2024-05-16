package com.example.pokexplore.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokexplore.R
import com.example.pokexplore.ui.PokemonRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.settings_description),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
            SettingsGroup(name = R.string.appearance) {
                SettingsClickableComp(
                    name = stringResource(R.string.theme),
                    icon = Icons.Filled.Palette,
                    iconDesc = stringResource(R.string.theme),
                ) {
                    navController.navigate(PokemonRoute.Theme.route)
                }
                SettingsClickableComp(
                    name = stringResource(R.string.theme),
                    icon = Icons.Filled.Palette,
                    iconDesc = stringResource(R.string.theme),
                ) {
                    navController.navigate(PokemonRoute.Theme.route)
                }
            }
            SettingsGroup(name = R.string.appearance) {
                SettingsClickableComp(
                    name = stringResource(R.string.theme),
                    icon = Icons.Filled.Palette,
                    iconDesc = stringResource(R.string.theme),
                ) {
                    navController.navigate(PokemonRoute.Theme.route)
                }
                SettingsClickableComp(
                    name = stringResource(R.string.theme),
                    icon = Icons.Filled.Palette,
                    iconDesc = stringResource(R.string.theme),
                ) {
                    navController.navigate(PokemonRoute.Theme.route)
                }
                SettingsClickableComp(
                    name = stringResource(R.string.theme),
                    icon = Icons.Filled.Palette,
                    iconDesc = stringResource(R.string.theme),
                ) {
                    navController.navigate(PokemonRoute.Theme.route)
                }
            }
        }
    }
}

@Composable
fun SettingsClickableComp(
    icon: ImageVector,
    iconDesc: String,
    name: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon,
                        contentDescription = iconDesc,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surfaceTint
                        ),
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    Icons.Rounded.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = stringResource(id = R.string.downloading_pokemon)
                )
            }
            //Divider()
        }
    }
}

@Composable
fun SettingsGroup(
    @StringRes name: Int,
    // to accept only composables compatible with column
    content: @Composable ColumnScope.() -> Unit ){
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(stringResource(id = name))
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4),
        ) {
            Column {
                content()
            }
        }
    }
}
