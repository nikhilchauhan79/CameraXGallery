package com.nikhilchauhan.cameraxcompose.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosVM @Inject constructor(private val repository: PhotosRepository) : ViewModel() {
  val photoCaptureState: MutableState<CaptureState?> = mutableStateOf(null)
  private val totalCurrentSession = mutableStateOf(0)
  private val albumCurrentSession = mutableStateOf("")
  private val albumId = mutableStateOf(0L)
  private val sessionEnd = mutableStateOf(false)
  private val currentPhoto = mutableStateOf("")

  init {
    onSessionStart()
  }

  private fun onSessionStart() {
    val time = System.currentTimeMillis()
    totalCurrentSession.value = 0
    sessionEnd.value = false
    albumCurrentSession.value = "Album_$time"
    albumId.value = time
  }

  fun savePhotoToDb(uri: Uri) {
    if (sessionEnd.value) {
      onSessionStart()
    }
    capturePhoto(uri)
  }

  private fun capturePhoto(uri: Uri) {
    val time = System.currentTimeMillis()
    totalCurrentSession.value++
    photoCaptureState.value = Success(uri)
    currentPhoto.value = "Photo_$time"
    repository.savePhoto(
      Photo(
        0, currentPhoto.value, time, albumCurrentSession.value, albumId.value, uri.path,
        totalCurrentSession.value
      )
    )
  }

  fun endSession() {
    sessionEnd.value = true
  }
}