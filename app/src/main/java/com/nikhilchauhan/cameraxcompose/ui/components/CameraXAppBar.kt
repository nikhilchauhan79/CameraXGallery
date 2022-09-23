package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nikhilchauhan.cameraxcompose.R

@Composable
fun CameraXAppBar(
  title: String
) {
  TopAppBar(title = {
    Text(text = title)
  }, navigationIcon = {
    IconButton(onClick = {

    }) {
      Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = stringResource(id = R.string.menu_icon)
      )
    }
  }
  )
}