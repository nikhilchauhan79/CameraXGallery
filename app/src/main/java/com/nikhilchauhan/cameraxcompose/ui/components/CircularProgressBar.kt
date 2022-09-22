package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraXProgressBar() {
  CircularProgressIndicator(
    modifier = Modifier
      .size(50.dp),
    strokeWidth = 8.dp,
    color = Color.Blue
  )
}