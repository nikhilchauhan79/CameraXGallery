package com.nikhilchauhan.cameraxcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.ui.GalleryVM
import com.nikhilchauhan.cameraxcompose.ui.screens.GalleryScreen
import com.nikhilchauhan.cameraxcompose.ui.theme.CameraXComposeTheme
import com.nikhilchauhan.cameraxcompose.utils.PermissionsUtils
import dagger.hilt.android.AndroidEntryPoint

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

      LaunchedEffect(key1 = Unit) {
        galleryVM.getPhotosFromDB()
      }
      CameraXComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          GalleryScreen(galleryVM.photosList.value)
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  CameraXComposeTheme {
    Greeting("Android")
  }
}