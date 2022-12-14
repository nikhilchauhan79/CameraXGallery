package com.nikhilchauhan.cameraxcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.nikhilchauhan.cameraxcompose.R.string
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.ui.GalleryVM
import com.nikhilchauhan.cameraxcompose.ui.screens.GalleryScreen
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.Error
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.InProgress
import com.nikhilchauhan.cameraxcompose.ui.theme.CameraXComposeTheme
import com.nikhilchauhan.cameraxcompose.utils.AlertUtils
import com.nikhilchauhan.cameraxcompose.utils.PermissionsUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      Log.i(AppConstants.MAIN_ACTIVITY, "Permission granted")
    } else {
      Log.i(AppConstants.MAIN_ACTIVITY, "Permission denied")
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    if (!PermissionsUtils.checkPermissions(this)) {
      PermissionsUtils.PERMISSIONS.onEach { permission ->
        requestPermissionLauncher.launch(permission)
      }
    }
    setContent {
      val galleryVM: GalleryVM = hiltViewModel()

      CameraXComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          galleryVM.apply {
            GalleryScreen(
              photoDbState.collectAsState().value, photoCaptureState.value,
              showProgress.value, {
              endSession()
            }, currentAlbum.collectAsState().value, { photo ->
              photo.albumId?.let { nnAlbumId ->
                getPhotosByAlbumId(nnAlbumId)
              }
            }, toolbarText.collectAsState().value, { newTitle ->
              toolbarText.value = newTitle
            }
            ) { captureState ->
              handleCaptureState(captureState, galleryVM) {
                showProgress.value = it
              }
            }
          }
        }
      }
    }
  }

  private fun handleCaptureState(
    captureState: CaptureState,
    galleryVM: GalleryVM,
    showProgress: (Boolean) -> Unit
  ) {
    when (captureState) {
      is Error -> {
        lifecycleScope.launch {
          AlertUtils.showToast(
            this@MainActivity,
            "Error Capturing Photo: ${captureState.message} ${captureState.error}"
          )
        }
        showProgress(false)
        Log.d(
          AppConstants.MAIN_ACTIVITY,
          "handleCaptureState: " + captureState.message + "Error" + captureState.error
        )
      }
      InProgress -> {
        lifecycleScope.launch {
          AlertUtils.showToast(this@MainActivity, getString(string.photo_capture_in_progress))
        }
        showProgress(true)
        Log.d(AppConstants.MAIN_ACTIVITY, "handleCaptureState: " + "photo capture in progress")
      }
      CaptureState.Init -> {
        showProgress(false)
      }
      is CaptureState.Success -> {
        lifecycleScope.launch {
          AlertUtils.showToast(
            this@MainActivity, getString(string.photo_capture_success)
          )
        }
        showProgress(false)
        galleryVM.savePhotoToDb(captureState.uri)
      }
    }
  }
}
