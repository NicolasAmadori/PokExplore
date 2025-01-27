package com.example.pokexplore.ui.screens.allpokemonlist

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.database.UserWithPokemons
import com.example.pokexplore.data.remote.OSMDataSource
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.utilities.LocationService
import com.example.pokexplore.utilities.pastelColours
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.util.Locale

data class TabItem(val icon: ImageVector, val stringId: Int, val index: Int)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AllPokemonListScreen(
    navController: NavHostController,
    allPokemonListState: AllPokemonListState,
    userState: UserState,
    countryCodeState: CountryCodeState,
    actions: AllPokemonListActions,
    locationService: LocationService
) {
    val ctx = LocalContext.current

    /* HTTP */
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
        if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(intent)
        }
    }

    val osmDataSource = koinInject<OSMDataSource>()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit){
        Log.d("AllPokemonListScreen", "request coordinates")
        locationService.requestCurrentLocation()
    }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(locationService.coordinates) {
        Log.d("AllPokemonListScreen", "changed coordinates")
        if (locationService.coordinates != null) {
            coroutineScope.launch {
                if (isOnline()) {
                    val countryCode = osmDataSource.getCountryISOCode(
                        locationService.coordinates!!.latitude,
                        locationService.coordinates!!.longitude
                    )

                    Log.d("AllPokemonListScreen", countryCode)
                    actions.setCountryCode(countryCode)
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
        }
    }


    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var searchText by remember { mutableStateOf("") } // Query for SearchBar
    var isSearchActive by remember { mutableStateOf(false) } // Active state for SearchBar
    val searchHistory = remember { mutableStateListOf("") }
    val selectedTypes = remember { mutableStateListOf<String>() }
    val selectedGenerations = remember { mutableStateListOf<String>() }

    fun isSearchCoherent(pokemon: Pokemon): Boolean{
        return (pokemon.name.lowercase().contains(searchText.trim().lowercase()) || pokemon.pokemonId.toString().contains(searchText.trim()))
                && (pokemon.types.any{selectedTypes.contains(it)} || selectedTypes.isEmpty())
                && (selectedGenerations.contains(pokemon.generation) || selectedGenerations.isEmpty())
    }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (!isSearchActive) 16.dp else 0.dp),
                query = searchText,
                onQueryChange = {
                    searchText = it
                },
                onSearch = {
                    if(searchHistory.contains(it)){
                        searchHistory.remove(it)
                    }
                    else if(searchHistory.size == 5){
                        searchHistory.removeAt(0)
                    }
                    searchHistory+= it
                    isSearchActive = false
                },
                active = isSearchActive,
                onActiveChange = {
                    isSearchActive = it
                },
                placeholder = {
                    Text(stringResource(R.string.searchbar_placeholder))
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            isSearchActive = !isSearchActive
                        },
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon_description))
                },
                trailingIcon = {
                    if (isSearchActive || searchText.trim().isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable {
                                searchText = ""
                                isSearchActive = false
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.search_close_description)
                        )
                    }
                }
            ) {
                Column{
                    Text(stringResource(R.string.type),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier.padding(8.dp))
                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        maxItemsInEachRow = 6,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        allPokemonListState.userWithPokemonsList
                            .flatMap { it.pokemon.types }
                            .distinct().forEach{
                                ChipItem(it, selectedTypes)
                            }
                    }
                    Divider()
                    Text(stringResource(R.string.generation),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier.padding(8.dp))
                    FlowRow(
                        modifier = Modifier.padding(8.dp),
                        maxItemsInEachRow = 6,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        allPokemonListState.userWithPokemonsList
                            .map { it.pokemon.generation }
                            .distinct().forEach{
                                ChipItem(it, selectedGenerations)
                            }
                    }
                    Divider()
                    searchHistory.reversed().forEach {
                        if (it.isNotEmpty()) {
                            Row(modifier = Modifier.padding(all = 14.dp)) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = stringResource(R.string.history_item_description)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = it)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.delete_history_item),
                                    modifier = Modifier.clickable {
                                        searchHistory.remove(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    ){ contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)){
            TabRow(selectedTabIndex = selectedTabIndex)
            {
                val tabs = listOf(
                    TabItem(Icons.Filled.Public, R.string.tab_all,0),
                    TabItem(Icons.Filled.GpsFixed,R.string.tab_near_me,1),
                    TabItem(Icons.Filled.FavoriteBorder,R.string.tab_favourites,2)
                )
                tabs.forEach { (icon, stringId, index) ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(stringResource(stringId))
                        },
                        icon = {
                            Icon(
                                icon,
                                contentDescription = stringResource(stringId),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp)
            ) {
                if(userState.user != null) {
                    var filteredList = when (selectedTabIndex) {
                        0 -> allPokemonListState.userWithPokemonsList.filter { it.user.email == userState.user.email }
                        1 -> allPokemonListState.userWithPokemonsList.filter { it.user.email == userState.user.email && (it.pokemon.countryCode == countryCodeState.countryCode || it.pokemon.countryCode == null) }
                        else -> allPokemonListState.userWithPokemonsList.filter { it.user.email == userState.user.email && it.isFavourite }
                    }
                    filteredList = filteredList.filter { isSearchCoherent(it.pokemon) }
                    items(filteredList) { userWithPokemon ->
                        PokemonCard(
                            userWithPokemon,
                            onClick = {
                                navController.navigate(PokemonRoute.PokemonDetails.buildRoute(userWithPokemon.pokemon.pokemonId))
                            },
                            onToggleFavourite = {
                                actions.toggleFavourite(userWithPokemon.user.email, userWithPokemon.pokemon.pokemonId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun PokemonCard(
    userWithPokemon: UserWithPokemons,
    onClick: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    val typeIcons = mapOf(
        "normal" to R.drawable.normal,
        "fire" to R.drawable.fire,
        "water" to R.drawable.water,
        "electric" to R.drawable.electric,
        "grass" to R.drawable.grass,
        "ice" to R.drawable.ice,
        "fighting" to R.drawable.fighting,
        "poison" to R.drawable.poison,
        "ground" to R.drawable.ground,
        "flying" to R.drawable.flying,
        "psychic" to R.drawable.psychic,
        "bug" to R.drawable.bug,
        "rock" to R.drawable.rock,
        "ghost" to R.drawable.ghost,
        "dragon" to R.drawable.dragon,
        "dark" to R.drawable.dark,
        "steel" to R.drawable.steel,
        "fairy" to R.drawable.fairy
    )

    val colorType = if (userWithPokemon.pokemon.types.size == 1) {
        userWithPokemon.pokemon.types.first()
    } else {
        userWithPokemon.pokemon.types.first { pastelColours.containsKey(it) && it != "normal" }
    }

    Card(
        onClick = onClick,
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(pastelColours[colorType]!!)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier= Modifier.padding(horizontal = 5.dp)
            ){
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${userWithPokemon.pokemon.pokemonId}.png"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Text(
                    text = userWithPokemon.pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "#${String.format("%03d", userWithPokemon.pokemon.pokemonId)}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                userWithPokemon.pokemon.types.forEach{ type ->
                    Image(
                        painter = painterResource(typeIcons[type]!!),
                        contentDescription = stringResource(R.string.type),
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                if(userWithPokemon.isCaptured){
                    Image(
                        painter = painterResource(R.drawable.full_pokeball),
                        contentDescription = stringResource(R.string.caught),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { onToggleFavourite() }) {
                    Icon(
                        imageVector = if(userWithPokemon.isFavourite) {
                            Icons.Outlined.Favorite
                        }
                        else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = stringResource(R.string.favourite_label),
                        tint = if (userWithPokemon.isFavourite) Color.Red else Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipItem(text: String, selectedItems: SnapshotStateList<String>) {
    FilterChip(
        onClick = {
            if(selectedItems.contains(text)){
                selectedItems.remove(text)
            }
            else {
                selectedItems.add(text)
            }
        },
        label = {
            Text(text)
        },
        selected = selectedItems.contains(text),
        leadingIcon = if (selectedItems.contains(text)) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        modifier= Modifier.padding(end = 4.dp)
    )

}