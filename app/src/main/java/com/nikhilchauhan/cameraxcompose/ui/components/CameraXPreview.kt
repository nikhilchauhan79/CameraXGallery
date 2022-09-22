package com.nikhilchauhan.cameraxcompose.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.nikhilchauhan.cameraxcompose.R
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.InProgress
import com.nikhilchauhan.cameraxcompose.utils.AlertUtils
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraXView(
  outputDirectory: File,
  executor: Executor,
  paddingValues: PaddingValues,
  captureState: CaptureState,
  onCaptureStateChanged: (CaptureState) -> Unit,
  showProgress: Boolean,
  onSessionStart: () -> Unit
) {
  val currentSessionStart by rememberUpdatedState(onSessionStart)

  LaunchedEffect(key1 = Unit) {
    currentSessionStart()
  }

  val lensFacing = CameraSelector.LENS_FACING_BACK
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val preview = Preview.Builder().build()
  val previewView = remember { PreviewView(context) }
  val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
  val cameraSelector = CameraSelector.Builder()
    .requireLensFacing(lensFacing)
    .build()

  LaunchedEffect(lensFacing) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
      lifecycleOwner,
      cameraSelector,
      preview,
      imageCapture
    )

    preview.setSurfaceProvider(previewView.surfaceProvider)
  }

  Box(
    contentAlignment = Alignment.BottomCenter,
    modifier = Modifier
      .fillMaxSize()
      .padding(paddingValues)
  ) {
    AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

    IconButton(
      modifier = Modifier.padding(bottom = 20.dp),
      onClick = {
        Log.i(AppConstants.CAMERA_X_VIEW, "button clicked")
        if (captureState !is InProgress) {
          onCaptureStateChanged(CaptureState.InProgress)
          takePhoto(
            imageCapture = imageCapture,
            outputDirectory = outputDirectory,
            executor = executor,
            onImageCaptured = onCaptureStateChanged
          )
        } else {
          AlertUtils.showToast(context, AppConstants.IMAGE_CAPTURE_IN_PROGRESS)
        }
      },
      content = {
        Icon(
          painter = painterResource(id = R.drawable.ic_outline_camera_24),
          contentDescription = stringResource(id = R.string.capture_photo),
          tint = White,
          modifier = Modifier
            .size(48.dp)
            .border(8.dp, White, CircleShape)
        )
      }
    )
  }
}

private fun takePhoto(
  imageCapture: ImageCapture,
  outputDirectory: File,
  executor: Executor,
  onImageCaptured: (CaptureState) -> Unit,
) {

  val photoFile = File(
    outputDirectory,
    "Photo_" + System.currentTimeMillis() + ".jpg"
  )

  val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

  imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
    override fun onError(exception: ImageCaptureException) {
      Log.e(AppConstants.CAMERA_X_VIEW, "error capturing photo: ", exception)
      onImageCaptured(CaptureState.Error(exception, exception.message.toString()))
    }

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
      val savedUri = Uri.fromFile(photoFile)
      onImageCaptured(CaptureState.Success(savedUri))
    }
  })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
  suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
      cameraProvider.addListener({
        continuation.resume(cameraProvider.get())
      }, ContextCompat.getMainExecutor(this))
    }
  }
