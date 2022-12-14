package com.nikhilchauhan.cameraxcompose.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState
import com.nikhilchauhan.cameraxcompose.ui.states.CaptureState.Success
import com.nikhilchauhan.cameraxcompose.ui.states.DbState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GalleryVM @Inject constructor(private val repository: PhotosRepository) : ViewModel() {
  private val _photosDbState = MutableStateFlow<DbState>(DbState.Init)
  val toolbarText = MutableStateFlow(AppConstants.APP_NAME)
  val photoDbState: StateFlow<DbState> = _photosDbState
  private val _isSessionFirstPhoto = mutableStateOf(false)
  private val isSessionFirstPhoto: State<Boolean> = _isSessionFirstPhoto
  private val photosFlow: MutableStateFlow<MutableList<Photo>> = MutableStateFlow(
    mutableListOf()
  )
  val currentAlbum = MutableStateFlow<MutableList<Photo>>(mutableListOf())
  val photoCaptureState: MutableState<CaptureState> = mutableStateOf(CaptureState.Init)
  private val totalCurrentSession = mutableStateOf(0)
  private val totalAlbums = mutableStateOf(0)
  private val albumCurrentSession = mutableStateOf("")
  private val albumId = mutableStateOf(0L)
  private val sessionEnd = mutableStateOf(false)
  val showProgress = mutableStateOf(false)
  private val currentPhoto = mutableStateOf("")

  init {
    setTotalAlbums()
    onSessionStart()
    getPhotosFromDB()
  }

  private fun onSessionStart() {
    _isSessionFirstPhoto.value = true
    val time = System.currentTimeMillis()
    totalCurrentSession.value = 0
    sessionEnd.value = false
    albumCurrentSession.value = "Album_$time"
    albumId.value = time
    totalAlbums.value++
  }

  private fun setTotalAlbums() {
    viewModelScope.launch(Dispatchers.IO) {
      val total = async {
        repository.getTotalAlbums()
      }
      withContext(Dispatchers.Main) {
        totalAlbums.value = total.await()
      }
    }
  }

  fun getPhotosByAlbumId(albumId: Long) {
    viewModelScope.launch(Dispatchers.IO) {
      currentAlbum.value.clear()
      currentAlbum.value.addAll(repository.getPhotosByAlbumId(albumId))
    }
  }

  fun savePhotoToDb(uri: Uri) {
    if (sessionEnd.value) {
      onSessionStart()
    }
    capturePhoto(uri)
    getPhotosFromDB()
  }

  private fun capturePhoto(uri: Uri) {
    viewModelScope.launch(Dispatchers.IO) {
      val time = System.currentTimeMillis()
      totalCurrentSession.value++
      photoCaptureState.value = Success(uri)
      currentPhoto.value = "Photo_$time"
      val photo = Photo(
        0, totalAlbums.value, currentPhoto.value, time, albumCurrentSession.value, albumId.value,
        uri.path,
        totalCurrentSession.value, isSessionFirstPhoto.value
      )
      repository.savePhoto(
        photo
      )
      _isSessionFirstPhoto.value = false
      getPhotosFromDB()
    }
  }

  fun endSession() {
    sessionEnd.value = true
  }

  private fun getPhotosFromDB() {
    viewModelScope.launch(Dispatchers.IO) {
      _photosDbState.emit(DbState.InProgress)
      photosFlow.value.clear()
      val photos = repository.getPhotos()
      photosFlow.value.addAll(photos)
      _photosDbState.emit(DbState.Success(photos))
    }
  }
}
