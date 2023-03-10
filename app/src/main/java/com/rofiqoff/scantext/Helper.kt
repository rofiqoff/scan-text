package com.rofiqoff.scantext

import android.app.Activity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

const val FILENAME_FORMAT = "dd-MMM-yyyy-hh-mm-ss"

fun ProcessCameraProvider.bind(
    lifecycleOwner: LifecycleOwner,
    preview: Preview,
    imageCapture: ImageCapture?,
) = try {
    unbindAll()
    bindToLifecycle(
        lifecycleOwner,
        CameraSelector.DEFAULT_BACK_CAMERA,
        preview,
        imageCapture
    )
} catch (e: IllegalStateException) {
    e.printStackTrace()
}

fun Activity.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
}

fun fileNameFormat() {
    SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
    ).format(System.currentTimeMillis())
}
