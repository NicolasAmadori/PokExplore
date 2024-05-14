package com.example.pokexplore.ui.screens.gpsMandatory

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.data.remote.OSMDataSource
import com.example.pokexplore.ui.PokemonRoute
import com.example.pokexplore.utilities.LocationService
import com.example.pokexplore.utilities.PermissionStatus
import com.example.pokexplore.utilities.StartMonitoringResult
import com.example.pokexplore.utilities.rememberPermission
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GpsMandatoryScreen(
    navController: NavHostController,
    gpsMandatoryState: GpsMandatoryState,
    setCountryCode: (String) -> Unit
) {

    val context = LocalContext.current

    val osmDataSource = koinInject<OSMDataSource>()

    val locationService = LocationService(context)
    val snackbarHostState = remember { SnackbarHostState() }
    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    fun isOnline(): Boolean {
        val connectivityManager = context
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
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    fun getCountryCode() = coroutineScope.launch {
        if (isOnline()) {
            if(locationService.coordinates != null) {
                setCountryCode(
                    osmDataSource.getCountryISOCode(locationService.coordinates!!.latitude, locationService.coordinates!!.longitude)
                )
            }
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

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                val res = locationService.requestCurrentLocation()
                if(res == StartMonitoringResult.GPSDisabled) {
                    showLocationDisabledAlert = true
                }
                else {
                    navController.navigate(PokemonRoute.AllPokemonList.route)
                }
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
            if(res == StartMonitoringResult.GPSDisabled) {
                showLocationDisabledAlert = true
            }
            else {
                getCountryCode()
                navController.navigate(PokemonRoute.AllPokemonList.route)
            }
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(gpsMandatoryState.countryCode?:"-")
                Text(
                    text = stringResource(R.string.gps_page_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.gps_page_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = ::requestLocation,
                    modifier = Modifier.size(width = 180.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.gps_page_button_label))
                }

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
                                data = Uri.fromParts("package", context.packageName, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            }
                        }
                        showPermissionPermanentlyDeniedSnackbar = false
                    }
                }
            }
        }
    }

}