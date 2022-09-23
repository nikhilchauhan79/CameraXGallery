package com.nikhilchauhan.cameraxcompose.repository

import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(private val photosDao: PhotosDao) : PhotosRepository {
  override suspend fun getPhotos(): List<Photo> =
    photosDao.getPhotos()

  override suspend fun savePhoto(photo: Photo) = photosDao.insert(photo)
  override suspend fun getPhotosByAlbumId(albumId: Long): List<Photo> =
    photosDao.getPhotosInAlbum(albumId)

  override suspend fun getTotalAlbums(): Int = photosDao.getTotalAlbums()
}