package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppProgressBar() {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.height(50.dp)
  ) {
    CircularProgressIndicator()
  }
}