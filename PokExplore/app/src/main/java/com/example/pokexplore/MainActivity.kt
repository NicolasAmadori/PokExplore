package com.example.pokexplore

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokexplore.data.models.Theme
import com.example.pokexplore.ui.BottomNavItem
import com.example.pokexplore.ui.BottomNavScreen
import com.example.pokexplore.ui.PokExploreViewModel
import com.example.pokexplore.ui.PokemonNavGraph
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.signup.SignUpViewModel
import com.example.pokexplore.ui.screens.theme.ThemeViewModel
import com.example.pokexplore.ui.theme.PokExploreTheme
import com.example.pokexplore.utilities.LocationService
import com.example.pokexplore.utilities.rememberPermission
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationService = LocationService(this)

        setContent {
            /* Theme*/
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            PokExploreTheme (
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /* Pokemon list */
                    val pkVm = koinViewModel<PokExploreViewModel>()
                    val pkState by pkVm.state.collectAsStateWithLifecycle()

                    /* Logged in user */
                    val signUpVm = koinViewModel<SignUpViewModel>()
                    val signUpState = signUpVm.state

                    /* Navigation */
                    val navController = rememberNavController()

                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            PokemonRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: PokemonRoute.AllPokemonList
                        }
                    }

                    /* HTTP */
//                    val snackbarHostState = remember { SnackbarHostState() }
//                    val osmDataSource = koinInject<OSMDataSource>()
//                    val dataStoreRepository = koinInject<DataStoreRepository>()
//
                    val ctx = LocalContext.current
//                    val coroutineScope = rememberCoroutineScope()
//                    fun getCountryCode() = coroutineScope.launch {
//                        if (isOnline()) {
//                            if(locationService.coordinates != null) {
//                                mainVm.setCountryCode(
//                                    osmDataSource.getCountryISOCode(locationService.coordinates!!.latitude, locationService.coordinates!!.longitude)
//                                )
//                            }
//                        } else {
//                            val res = snackbarHostState.showSnackbar(
//                                message = "No Internet connectivity",
//                                actionLabel = "Go to Settings",
//                                duration = SnackbarDuration.Long
//                            )
//                            if (res == SnackbarResult.ActionPerformed) {
//                                openWirelessSettings()
//                            }
//                        }
//                    }
//
//                    LaunchedEffect(locationService.coordinates) {
//                        getCountryCode()
//                    }
//
//                    /* GPS */
//                    val locationPermission = rememberPermission(
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) { status ->
//                        when (status) {
//                            PermissionStatus.Granted -> {
//                                val res = locationService.requestCurrentLocation()
//                            }
//
//                            PermissionStatus.Denied -> {}
//                            PermissionStatus.PermanentlyDenied -> {}
//                            PermissionStatus.Unknown -> {}
//                        }
//                    }
//
//                    fun requestLocation() {
//                        if (locationPermission.status.isGranted) {
//                            val res = locationService.requestCurrentLocation()
//                            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
//                        } else {
//                            navController.navigate(PokemonRoute.GpsMandatory.route)
//                        }
//                    }
//
//                    LaunchedEffect(true) {
//                        requestLocation()
//                    }

                    /* Camera */
                    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
                        if (status.isGranted) {
                            //cameraLauncher.captureImage()
                        } else {
                            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
                        }
                    }

                    /* UI */
                    Scaffold(
                        bottomBar = { if (shouldShowBottomNav(currentRoute)) BottomNavScreen(navController)}
                    ){ contentPadding ->
                        PokemonNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
//                            startDestination = PokemonRoute.GpsMandatory.route
                            startDestination = if(signUpState.user == null) {
                                PokemonRoute.SignIn.route
                            } else if(pkState.pokemons.isEmpty()){
                                PokemonRoute.Loading.route
                            } else {
                                PokemonRoute.AllPokemonList.route
                            }
                        )
                    }
                }
            }
        }
    }

    private fun shouldShowBottomNav(route: PokemonRoute): Boolean {
        return BottomNavItem.items.any { it.route == route.route }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}