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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.ui.components.BottomNavBar
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXAppBar
import com.nikhilchauhan.cameraxcompose.ui.components.CameraXProgressBar
import com.nikhilchauhan.cameraxcompose.ui.navigation.NavHostGraph
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Error
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.InProgress
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Init
import com.nikhilchauhan.cameraxcompose.ui.states.DbState.Success
import com.nikhilchauhan.cameraxcompose.ui.states.NavigationItem
import com.nikhilchauhan.cameraxcompose.utils.AppUtils.formatDate
import java.io.File

@Composable
fun GalleryScreen(
  dbState: DbState,
  captureState: CaptureState,
  showProgress: Boolean,
  onSessionStart: () -> Unit,
  photosList: List<Photo>,
  onPhotoClick: (Photo) -> Unit,
  toolbarTitle: String,
  onToolbarTextChanged: (String) -> Unit,
  onImageCaptureStateChanged: (CaptureState) -> Unit,
) {
  val scaffoldState: ScaffoldState = rememberScaffoldState()
  val navController = rememberNavController()
  var appBarIcon by remember {
    mutableStateOf(Icons.Filled.Menu)
  }
  appBarIcon = if (navController.previousBackStackEntry != null) {
    Icons.Filled.ArrowBack
  } else {
    Icons.Filled.Menu
  }
  Scaffold(
    scaffoldState = scaffoldState,
    bottomBar = {
      BottomNavBar(navController)
    }, topBar = {
    CameraXAppBar(toolbarTitle, appBarIcon) {
      navController.navigateUp()
    }
  }
  ) { paddingValues ->
    NavHostGraph(
      navController, dbState, paddingValues, captureState, onImageCaptureStateChanged, showProgress,
      onSessionStart, onPhotoClick, photosList, onToolbarTextChanged
    )
  }
}

@Composable
fun PhotosList(
  dbState: DbState,
  showProgress: Boolean,
  onPhotoClick: (Photo) -> Unit,
  navController: NavHostController
) {
  HandleDbState(dbState, showProgress, onPhotoClick, navController)
}

@Composable
private fun HandleDbState(
  dbState: DbState,
  showProgress: Boolean,
  onPhotoClick: (Photo) -> Unit,
  navController: NavHostController
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
      if (dbState.list.isEmpty()) {
        Text(
          text = "No photos found, capture some pics then they will appear here",
          style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 20.sp),
          modifier = Modifier.padding(all = 8.dp)
        )
      }
      PhotosGrid(photos = dbState.list, navController, onPhotoClick)
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotosGrid(
  photos: List<Photo>,
  navController: NavController,
  onPhotoClick: (Photo) -> Unit
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(count = 2),
    modifier = Modifier
      .fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    photos.forEachIndexed { index, photo ->
      if (photo.isSessionFirst) {
        val formattedDate = formatDate(photo.timeStamp ?: System.currentTimeMillis())
        item(span = {
          GridItemSpan(2)
        }, key = photo.albumId) {
          val padding = if (index != 0) {
            12.dp
          } else 8.dp
          Card(
            shape = RoundedCornerShape(size = 8.dp), modifier = Modifier
            .padding(top = padding, bottom = 4.dp)
            .height(40.dp)
            .fillMaxWidth(),
            elevation = 4.dp, backgroundColor = MaterialTheme.colors.secondary
          ) {
            Row(
              modifier = Modifier
                .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Album " + photo.totalAlbums, style = MaterialTheme.typography.h6,
                modifier = Modifier
                  .padding(horizontal = 16.dp)
                  .semantics {
                    contentDescription = "Album Name"
                  }
              )

              Text(
                text = formattedDate, style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 16.dp)
              )
            }
          }
        }
      }

      item(key = photo.uid) {
        Card(
          modifier = Modifier.fillMaxSize(),
          shape = RoundedCornerShape(8.dp),
          elevation = 8.dp, onClick = {
          onPhotoClick(photo)
          navController.navigate(NavigationItem.Album.route)
        }
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
      modifier = Modifier
        .fillMaxSize(1f)
        .semantics {
          contentDescription = "Image"
        }
    )
  }
}
