package com.nikhilchauhan.cameraxcompose.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import java.io.File

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AlbumScreen(
  photosList: List<Photo>
) {
  Log.d("TAG", "AlbumScreen: "+photosList.size)
  val pagerState = rememberPagerState()

  HorizontalPager(
    count = photosList.size, state = pagerState, modifier = Modifier.fillMaxSize()
  ) { page ->
    val painter = rememberAsyncImagePainter(model = photosList[page].path?.let { File(it) })
    Column(modifier = Modifier.fillMaxSize()) {
      Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}