package com.example.pokexplore.ui.screens.profile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.pokexplore.R
import com.example.pokexplore.ui.PieChart
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.allpokemonlist.PokemonCard
import com.example.pokexplore.utilities.rememberCameraLauncher
import com.example.pokexplore.utilities.rememberPermission
import com.example.pokexplore.utilities.saveImageToStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    state: UserState,
    pokemonState: PokemonState,
    actions: ProfileActions
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberCameraLauncher{
        Log.d("ProfileScreen", it.toString())
        actions.setProfilePicUrl(state.user?.email ?: "", it.toString())
    }

    //Gallery
//    fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
//        val uniqueFileName = "${UUID.randomUUID()}.jpg"
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val outputStream = context.openFileOutput(uniqueFileName, Context.MODE_PRIVATE)
//        return try {
//            inputStream?.use { input ->
//                outputStream.use { output ->
//                    input.copyTo(output)
//                }
//            }
//            val savedUri = FileProvider.getUriForFile(context, context.packageName + ".provider", File(context.filesDir, uniqueFileName))
//            savedUri
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                saveImageToStorage(it, context.applicationContext.contentResolver)
                actions.setProfilePicUrl(state.user?.email ?: "", it.toString())
            }
        }
    )

    //Camera
    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() =
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }

    var showMenu by remember { mutableStateOf(false) }

    @Composable
    fun ProfileImage(profilePicUrl: Uri?) {
        Log.d("ProfileScreen", (profilePicUrl != null && profilePicUrl.path != null).toString())
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    showMenu = true
                },
            contentAlignment = Alignment.Center
        ) {
            if (profilePicUrl != null && profilePicUrl.path != null) {
                AsyncImage(
                    ImageRequest.Builder(context)
                        .data(profilePicUrl)
                        .crossfade(true)
                        .build(),
                    "Captured image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.bottom_nav_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        actions.logOut{
                            navController.navigate(PokemonRoute.SignIn.route)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = stringResource(R.string.logout_description)
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(PokemonRoute.Settings.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.user?.let { user ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(8),
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
//                            Text(state.user.profilePicUrl?: "Niente")
                            ProfileImage(state.user.profilePicUrl?.toUri())

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Take Photo") },
                                    onClick = {
                                        showMenu = false
                                        takePicture()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Pick from Gallery") },
                                    onClick = {
                                        showMenu = false

                                        imagePickerLauncher.launch("image/*")
                                    }
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "${user.firstName} ${user.lastName}",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.labelMedium,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        PieChart(
                            data = listOf(
                                Triple(stringResource(R.string.not_caught_pokemons), pokemonState.userWithPokemonsList.count { !it.isCaptured && it.user.email == user.email }, MaterialTheme.colorScheme.primaryContainer),
                                Triple(stringResource(R.string.caught_pokemons), pokemonState.userWithPokemonsList.count { it.isCaptured && it.user.email == user.email  }, MaterialTheme.colorScheme.onPrimaryContainer)
                            ),
                            radiusOuter = 50.dp,
                            chartBarWidth = 20.dp,
                            animDuration = 300,
                        )
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                        Text(
                            text = stringResource(R.string.caught_pokemons_label),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                        ) {
                            items(pokemonState.userWithPokemonsList.filter { it.user.email == user.email && it.isCaptured }) { userWithPokemon ->
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
    }
}

