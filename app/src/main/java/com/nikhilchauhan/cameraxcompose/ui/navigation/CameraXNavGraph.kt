package com.nikhilchauhan.cameraxcompose.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nikhilchauhan.cameraxcompose.constants.AppConstants.NavItemRoutes
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXView
import com.nikhilchauhan.cameraxcompose.ui.screens.AlbumScreen
import com.nikhilchauhan.cameraxcompose.ui.screens.PhotosList
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import com.nikhilchauhan.cameraxcompose.ui.states.NavigationItem
import com.nikhilchauhan.cameraxcompose.ui.states.NavigationItem.CapturePhoto
import com.nikhilchauhan.cameraxcompose.ui.states.NavigationItem.Gallery
import com.nikhilchauhan.cameraxcompose.utils.AppUtils
import java.util.concurrent.Executors

@Composable
fun NavHostGraph(
  navController: NavHostController,
  dbState: DbState,
  paddingValues: PaddingValues,
  captureState: CaptureState,
  onImageCaptureStateChanged: (CaptureState) -> Unit,
  showProgress: Boolean,
  onSessionStart: () -> Unit,
  onPhotoClick: (Photo) -> Unit,
  photosList: List<Photo>,
  onToolbarTextChanged: (String) -> Unit
) {
  val context = LocalContext.current
  val outputDirectory = AppUtils.getOutputDirectory(context)
  val cameraExecutor = Executors.newSingleThreadExecutor()
  NavHost(navController, startDestination = NavItemRoutes.GALLERY) {
    composable(NavItemRoutes.GALLERY) {
      PhotosList(dbState, showProgress, paddingValues, onPhotoClick, navController)
      onToolbarTextChanged(stringResource(id = Gallery.title))
    }
    composable(NavItemRoutes.CAPTURE_PHOTO) {
      CameraXView(
        outputDirectory = outputDirectory,
        executor = cameraExecutor,
        captureState = captureState,
        onCaptureStateChanged = onImageCaptureStateChanged, paddingValues = paddingValues,
        showProgress = showProgress,
        onSessionStart = onSessionStart
      )
      onToolbarTextChanged(stringResource(id = CapturePhoto.title))
    }
    composable(NavItemRoutes.ALBUM) {
      AlbumScreen(photosList, paddingValues)
      onToolbarTextChanged(stringResource(id = NavigationItem.Album.title))
    }
  }
}
