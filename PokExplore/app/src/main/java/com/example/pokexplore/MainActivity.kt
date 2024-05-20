package com.example.pokexplore

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokexplore.data.models.Theme
import com.example.pokexplore.ui.BottomNavItem
import com.example.pokexplore.ui.BottomNavScreen
import com.example.pokexplore.ui.PokemonNavGraph
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.ui.screens.theme.ThemeViewModel
import com.example.pokexplore.ui.theme.PokExploreTheme
import com.example.pokexplore.utilities.LocationService
import com.example.pokexplore.utilities.PermissionStatus
import com.example.pokexplore.utilities.StartMonitoringResult
import com.example.pokexplore.utilities.rememberPermission
import org.koin.androidx.compose.koinViewModel

class MainActivity : FragmentActivity() {
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
                    val ctx = LocalContext.current

                    /* Navigation */
                    val navController = rememberNavController()

                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            PokemonRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: PokemonRoute.Loading
                        }
                    }

                    /* HTTP */
//                    val snackbarHostState = remember { SnackbarHostState() }
//                    val osmDataSource = koinInject<OSMDataSource>()
//                    val dataStoreRepository = koinInject<DataStoreRepository>()
//

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
                    /* GPS */
                    val snackbarHostState = remember { SnackbarHostState() }
                    var showLocationDisabledAlert by remember { mutableStateOf(false) }
                    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
                    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

                    val locationPermission = rememberPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) { status ->
                        when (status) {
                            PermissionStatus.Granted -> {
                                val res = locationService.requestCurrentLocation()
                                showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                            }

                            PermissionStatus.Denied ->
                                showPermissionDeniedAlert = true

                            PermissionStatus.PermanentlyDenied ->
                                showPermissionPermanentlyDeniedSnackbar = true

                            PermissionStatus.Unknown -> {}
                        }
                    }

                    fun requestLocation() {
                        if (locationPermission.status.isGranted) {
                            val res = locationService.requestCurrentLocation()
                            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                        } else {
                            locationPermission.launchPermissionRequest()
                        }
                    }

                    LaunchedEffect(Unit) {
                        requestLocation()
                    }

                    /* UI */
                    Scaffold(
                        bottomBar = { if (shouldShowBottomNav(currentRoute)) BottomNavScreen(navController)}
                    ){ contentPadding ->
                        PokemonNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
                            startDestination = PokemonRoute.Loading.route
                        )
                        if (showLocationDisabledAlert) {
                            AlertDialog(
                                title = { Text("Location disabled") },
                                text = { Text("Location must be enabled to get your current location in the app.") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        locationService.openLocationSettings()
                                        showLocationDisabledAlert = false
                                    }) {
                                        Text("Enable")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showLocationDisabledAlert = false }) {
                                        Text("Dismiss")
                                    }
                                },
                                onDismissRequest = { showLocationDisabledAlert = false }
                            )
                        }

                        if (showPermissionDeniedAlert) {
                            AlertDialog(
                                title = { Text("Location permission denied") },
                                text = { Text("Location permission is required to get your current location in the app.") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        locationPermission.launchPermissionRequest()
                                        showPermissionDeniedAlert = false
                                    }) {
                                        Text("Grant")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showPermissionDeniedAlert = false }) {
                                        Text("Dismiss")
                                    }
                                },
                                onDismissRequest = { showPermissionDeniedAlert = false }
                            )
                        }
                        if (showPermissionPermanentlyDeniedSnackbar) {
                            LaunchedEffect(snackbarHostState) {
                                val res = snackbarHostState.showSnackbar(
                                    "Location permission is required.",
                                    "Go to Settings",
                                    duration = SnackbarDuration.Long
                                )
                                if (res == SnackbarResult.ActionPerformed) {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", ctx.packageName, null)
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    if (intent.resolveActivity(ctx.packageManager) != null) {
                                        ctx.startActivity(intent)
                                    }
                                }
                                showPermissionPermanentlyDeniedSnackbar = false
                            }
                        }
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