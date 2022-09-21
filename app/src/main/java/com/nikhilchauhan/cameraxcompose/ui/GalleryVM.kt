package com.nikhilchauhan.cameraxcompose.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
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

  fun getPhotosFromDB() {
    viewModelScope.launch(Dispatchers.IO) {
      _photosList.value = DbState.InProgress
      _photosList.value = DbState.Success(repository.getPhotos())
    }
  }

  fun createAlbumsMap() {
  }
}
