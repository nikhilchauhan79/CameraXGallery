package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.nikhilchauhan.cameraxcompose.R

@Composable
fun CameraXAppBar(
  title: String,
  navigationIcon: ImageVector,
  onNavBarIconClick: () -> Unit
) {
  TopAppBar(title = {
    Text(text = title)
  }, navigationIcon = {
    IconButton(onClick = {
      onNavBarIconClick()
    }) {
      Icon(
        imageVector = navigationIcon,
        contentDescription = stringResource(id = R.string.menu_icon)
      )
    }
  }
  )
}