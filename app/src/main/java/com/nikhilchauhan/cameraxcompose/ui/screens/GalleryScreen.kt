package com.nikhilchauhan.cameraxcompose.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.ui.components.BottomNavBar
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXAppBar
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXView
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Error
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.InProgress
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Init
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Success
import com.nikhilchauhan.cameraxcompose.utils.AppUtils.getOutputDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

@Composable
fun GalleryScreen(dbState: DbState) {
  val scaffoldState: ScaffoldState = rememberScaffoldState()
  val navController = rememberNavController()
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    scaffoldState = scaffoldState,
    bottomBar = {
      BottomNavBar(navController)
    }, topBar = {
    CameraXAppBar()
  }
  ) { paddingValues ->
    NavHostGraph(navController, dbState, paddingValues) { uri ->
      coroutineScope.launch(Dispatchers.Main) {
        Toast.makeText(context, "Image capture success" + uri.path, Toast.LENGTH_LONG).show()
      }
    }
  }
}

@Composable
fun NavHostGraph(
  navController: NavHostController,
  dbState: DbState,
  paddingValues: PaddingValues,
  onImageCapture: (Uri) -> Unit
) {
  val context = LocalContext.current
  val outputDirectory = getOutputDirectory(context)
  val cameraExecutor = Executors.newSingleThreadExecutor()
  NavHost(navController, startDestination = AppConstants.NavItemRoutes.GALLERY) {
    composable(AppConstants.NavItemRoutes.GALLERY) {
      HandleDbState(dbState)
    }
    composable(AppConstants.NavItemRoutes.CAPTURE_PHOTO) {
      CameraXView(
        outputDirectory = outputDirectory,
        executor = cameraExecutor,
        onError = { Log.e(AppConstants.CAMERA_X_VIEW, "error capturing image:", it) },
        onImageCaptured = onImageCapture, paddingValues = paddingValues
      )
    }
  }
}

@Composable
private fun HandleDbState(dbState: DbState) {
  when (dbState) {
    is Error -> {
    }
    InProgress -> {
    }
    Init -> {
    }
    is Success -> {
      PhotosGrid(photos = dbState.list)
    }
  }
}

@Composable
fun PhotosGrid(photos: List<Photo>) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(count = 3),
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    items(photos, key = { photo ->
      photo.uid
    }) { photo ->
      PhotoItem(photo)
    }
  }
}

@Composable
fun PhotoItem(photo: Photo) {
  val painter = rememberAsyncImagePainter(model = photo.path?.let { File(it) })
  Column(modifier = Modifier.fillMaxSize()) {
    Image(
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier = Modifier.clip(CircleShape)
    )
  }
}
