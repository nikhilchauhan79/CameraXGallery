package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DotsIndicator(
  album: List<Photo>,
  pagerState: PagerState,
  scope: CoroutineScope,
  modifier: Modifier,
) {
  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
    contentPadding = PaddingValues(bottom = 16.dp)
  ) {
    itemsIndexed(album) { index, _ ->
      if (pagerState.currentPage == index) {
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.secondary)
            .clickable {
              scope.launch {
                pagerState.animateScrollToPage(page = index)
              }
            }
        )
      } else {
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.5f))
            .clickable {
              scope.launch {
                pagerState.animateScrollToPage(page = index)
              }
            }
        )
      }

      if (index != album.size - 1) {
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
      }
    }
  }
}