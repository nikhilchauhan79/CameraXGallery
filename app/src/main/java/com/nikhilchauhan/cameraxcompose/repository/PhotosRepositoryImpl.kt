package com.nikhilchauhan.cameraxcompose.repository

import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(private val photosDao: PhotosDao) : PhotosRepository {
  override fun getPhotos(): List<Photo> =
    photosDao.getPhotos()

  override fun savePhoto(photo: Photo) = photosDao.insert(photo)
}