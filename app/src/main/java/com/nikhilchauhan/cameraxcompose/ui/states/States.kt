package com.nikhilchauhan.cameraxcompose.ui.states

import android.net.Uri
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo

sealed class CaptureState {
  object InProgress : CaptureState()
  data class Success(val uri: Uri) : CaptureState()
  data class Error(
    val error: Exception,
    val message: String
  ) : CaptureState()
}

sealed class DbState {
  object InProgress : DbState()
  object Init : DbState()
  data class Success(val list: List<Photo>) : DbState()
  data class Error(
    val error: Exception,
    val message: String
  ) : DbState()
}