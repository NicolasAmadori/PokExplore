package com.example.pokexplore.ui.screens.catchPokemon

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.pokexplore.ui.PokemonRoute
import java.security.MessageDigest


@Composable
fun CatchPokemonScreen(navController: NavHostController) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder().build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                CameraAnalyzer(context){
                    val pokemonCode = generatePokemonCode(it)
                    navController.navigate(PokemonRoute.PokemonDetails.buildRoute(pokemonCode)) //TODO: add qrcode conversion to pokemonId
                }
            )

            runCatching {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            }.onFailure {
                Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
            }
            previewView
        }
    )
}

private fun generatePokemonCode(qrContent: String): Int {
    val md5Hash = calculateMD5(qrContent)
    val firstThreeDigits = md5Hash.filter { it.isDigit() }.substring(0,3).toInt()
    return mapToPokemonCode(firstThreeDigits, 0, 999, 1, 386)
}

private fun calculateMD5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    val byteArray = md.digest(input.toByteArray())
    return byteArray.joinToString("") { "%02x".format(it) }
}

private fun mapToPokemonCode(input: Int, start1: Int, end1: Int, start2: Int, end2: Int): Int {
    val slope = (end2 - start2).toDouble() / (end1 - start1)
    return (start2 + slope * (input - start1)).toInt()
}


