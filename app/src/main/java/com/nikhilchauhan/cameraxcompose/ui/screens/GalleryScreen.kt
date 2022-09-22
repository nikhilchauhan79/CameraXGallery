package com.nikhilchauhan.cameraxcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXProgressBar
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXView
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Error
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.InProgress
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Init
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Success
import com.nikhilchauhan.cameraxcompose.utils.AppUtils.formatDate
import com.nikhilchauhan.cameraxcompose.utils.AppUtils.getOutputDirectory
import java.io.File
import java.util.concurrent.Executors

@Composable
fun GalleryScreen(
  dbState: DbState,
  captureState: CaptureState,
  showProgress: Boolean,
  onImageCaptureStateChanged: (CaptureState) -> Unit,
) {
  val scaffoldState: ScaffoldState = rememberScaffoldState()
  val navController = rememberNavController()

  Scaffold(
    scaffoldState = scaffoldState,
    bottomBar = {
      BottomNavBar(navController)
    }, topBar = {
    CameraXAppBar()
  }
  ) { paddingValues ->
    NavHostGraph(
      navController, dbState, paddingValues, captureState, onImageCaptureStateChanged, showProgress
    )
  }
}

@Composable
fun NavHostGraph(
  navController: NavHostController,
  dbState: DbState,
  paddingValues: PaddingValues,
  captureState: CaptureState,
  onImageCaptureStateChanged: (CaptureState) -> Unit,
  showProgress: Boolean
) {
  val context = LocalContext.current
  val outputDirectory = getOutputDirectory(context)
  val cameraExecutor = Executors.newSingleThreadExecutor()
  NavHost(navController, startDestination = AppConstants.NavItemRoutes.GALLERY) {
    composable(AppConstants.NavItemRoutes.GALLERY) {
      HandleDbState(dbState, showProgress, paddingValues)
    }
    composable(AppConstants.NavItemRoutes.CAPTURE_PHOTO) {
      CameraXView(
        outputDirectory = outputDirectory,
        executor = cameraExecutor,
        captureState = captureState,
        onCaptureStateChanged = onImageCaptureStateChanged, paddingValues = paddingValues,
        showProgress = showProgress
      )
    }
  }
}

@Composable
private fun HandleDbState(
  dbState: DbState,
  showProgress: Boolean,
  paddingValues: PaddingValues
) {
  when (dbState) {
    is Error -> {
    }
    InProgress -> {
      CameraXProgressBar()
    }
    Init -> {
    }
    is Success -> {
      PhotosGrid(photos = dbState.list, paddingValues)
    }
  }
}

@Composable
fun PhotosGrid(
  photos: List<Photo>,
  paddingValues: PaddingValues
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(count = 2),
    modifier = Modifier
      .fillMaxSize()
      .padding(paddingValues),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    photos.forEach { photo ->
      if (photo.isSessionFirst) {
        val formattedDate = formatDate(photo.timeStamp ?: System.currentTimeMillis())
        item(span = {
          GridItemSpan(2)
        }, key = photo.timeStamp) {
          Row(
            modifier = Modifier
              .padding(vertical = 8.dp)
              .height(40.dp)
              .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = formattedDate.first, style = MaterialTheme.typography.h6,
              modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
              text = formattedDate.second, style = MaterialTheme.typography.h6,
              modifier = Modifier.padding(end = 16.dp)
            )
          }
        }
      }

      item(key = {
        photo.uid
      }) {
        Card(
          modifier = Modifier.fillMaxSize(),
          shape = RoundedCornerShape(8.dp),
          elevation = 8.dp
        ) {
          PhotoItem(photo)
        }
      }
    }
  }
}

@Composable
fun PhotoItem(
  photo: Photo
) {
  val painter = rememberAsyncImagePainter(model = photo.path?.let { File(it) })
  Column(modifier = Modifier.height(280.dp)) {
    Image(
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.FillBounds,
      modifier = Modifier.fillMaxSize(1f)
    )
  }
}
