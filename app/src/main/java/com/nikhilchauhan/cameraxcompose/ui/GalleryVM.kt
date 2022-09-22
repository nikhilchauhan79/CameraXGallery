package com.nikhilchauhan.cameraxcompose.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.Success
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryVM @Inject constructor(private val repository: PhotosRepository) : ViewModel() {
  private val _photosList = mutableStateOf<DbState>(DbState.Init)
  val photosList: State<DbState> = _photosList
  private val photosMap = MutableStateFlow<MutableMap<Long?, List<Photo>>>(mutableMapOf())

  val photoCaptureState: MutableState<CaptureState> = mutableStateOf(CaptureState.Init)
  private val totalCurrentSession = mutableStateOf(0)
  private val albumCurrentSession = mutableStateOf("")
  private val albumId = mutableStateOf(0L)
  private val sessionEnd = mutableStateOf(false)
  val showProgress = mutableStateOf(false)
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
    getPhotosFromDB()
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

  fun getPhotosFromDB() {
    viewModelScope.launch(Dispatchers.IO) {
      _photosList.value = DbState.InProgress
      _photosList.value = DbState.Success(repository.getPhotos())
    }
  }

  fun createAlbumsMap() {
  }
}
