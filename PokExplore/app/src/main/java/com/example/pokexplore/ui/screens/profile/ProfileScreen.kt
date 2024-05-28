package com.example.pokexplore.ui.screens.profile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.pokexplore.R
import com.example.pokexplore.ui.PieChart
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.allpokemonlist.PokemonCard
import com.example.pokexplore.utilities.rememberPermission
import java.io.ByteArrayOutputStream
import java.io.File

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

    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    fun getTempUri(context: Context): Uri? {
        val directory = context.cacheDir
        directory.mkdirs()
        val file = File.createTempFile(
            "image_" + System.currentTimeMillis().toString(),
            ".jpg",
            directory
        )
        return FileProvider.getUriForFile(
            context,
            authority,
            file
        )
    }

    val savedImageUri = remember { mutableStateOf<Uri?>(null) }
    fun saveImage(uri: Uri) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        savedImageUri.value = uri // Salva l'URI dell'immagine
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageByteArray: ByteArray = outputStream.toByteArray()
        editor.putString("SAVED_IMAGE", Base64.encodeToString(imageByteArray, Base64.DEFAULT))
        editor.apply()
    }

    fun loadImage(): Bitmap? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val imageString = sharedPreferences.getString("SAVED_IMAGE", null)
        return imageString?.let { encodedImage ->
            val imageByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        }
    }

    LaunchedEffect(state.user) {
        state.user?.let { user ->
            //actions.login(user.email)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                saveImage(it) // Salva l'immagine
                actions.setProfilePicUrl(state.user?.email ?: "", uri.toString()) // Salva l'URL dell'immagine nel database
            }
        }
    )

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            if (isSaved) {
                tempUri.value?.let {
                    saveImage(it) // Salva l'immagine
                }
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val tmpUri = getTempUri(context)
                tempUri.value = tmpUri
                takePhotoLauncher.launch(tempUri.value)
            } else {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    var showMenu by remember { mutableStateOf(false) }

    @Composable
    fun ProfileImage() {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    showMenu = true
                },
            contentAlignment = Alignment.Center
        ) {
            imageBitmap?.let {
                Image(
                    painter = rememberImagePainter(data = it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            } ?: run {
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
                        actions.logOut()
                        navController.navigate(PokemonRoute.SignIn.route)
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
                            ProfileImage()

                            // Menu per selezionare fotocamera o galleria
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Take Photo") },
                                    onClick = {
                                        showMenu = false
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
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

