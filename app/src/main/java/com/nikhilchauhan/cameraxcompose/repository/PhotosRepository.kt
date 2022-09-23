package com.nikhilchauhan.cameraxcompose.repository

import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo

interface PhotosRepository {
  suspend fun getPhotos(): List<Photo>
  suspend fun savePhoto(photo: Photo)
  suspend fun getPhotosByAlbumId(albumId: Long): List<Photo>
}