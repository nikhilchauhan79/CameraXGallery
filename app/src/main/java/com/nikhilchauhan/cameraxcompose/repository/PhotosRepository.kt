package com.nikhilchauhan.cameraxcompose.repository

import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo

interface PhotosRepository {
  fun getPhotos(): List<Photo>
  fun savePhoto(photo: Photo)
}