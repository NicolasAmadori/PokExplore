package com.example.pokexplore.utilities

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID

interface CameraLauncher {
    val capturedImageUri: Uri
    fun captureImage()
}

@Composable
fun rememberCameraLauncher(onPhotoTaken: (uri : Uri) -> Unit): CameraLauncher {
    val ctx = LocalContext.current
    val imageUri = remember {
        val imageFile = File.createTempFile("tmp_image", ".jpg", ctx.externalCacheDir)
        FileProvider.getUriForFile(ctx, ctx.packageName + ".provider", imageFile)
    }
    var capturedImageUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { pictureTaken ->
            if (pictureTaken) {
                capturedImageUri = imageUri
                saveImageToStorage(capturedImageUri, ctx.applicationContext.contentResolver)
                onPhotoTaken(imageUri)
            }
        }

    val cameraLauncher by remember {
        derivedStateOf {
            object : CameraLauncher {
                override val capturedImageUri = capturedImageUri
                override fun captureImage() = cameraActivityLauncher.launch((imageUri))
            }
        }
    }
    return cameraLauncher
}
