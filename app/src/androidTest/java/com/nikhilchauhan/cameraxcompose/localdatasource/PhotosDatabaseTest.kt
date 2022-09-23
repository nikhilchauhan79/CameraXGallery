package com.nikhilchauhan.cameraxcompose.localdatasource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PhotosDatabaseTest {

  private lateinit var photosDao: PhotosDao
  private lateinit var photosDatabase: PhotosDatabase

  companion object {
    val samplePhotos = listOf<Photo>(
      Photo(1, "Test Photo", System.currentTimeMillis(), "Test Album", 10L, "/fake/path", 5, true),
      Photo(2, "Test Photo", System.currentTimeMillis(), "Test Album", 10L, "/fake/path", 2, false),
      Photo(3, "Test Photo", System.currentTimeMillis(), "Test Album", 20L, "/fake/path", 3, true),
      Photo(4, "Test Photo", System.currentTimeMillis(), "Test Album", 20L, "/fake/path", 4, false)
    )
  }

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    photosDatabase = Room.inMemoryDatabaseBuilder(context, PhotosDatabase::class.java).build()
    photosDao = photosDatabase.photosDao()
  }

  @After
  @Throws(IOException::class)
  fun tearDown() {
    photosDatabase.close()
  }

  @Test
  fun add_and_read_photo_from_the_database() {
    val photo =
      samplePhotos[0]
    photosDao.insert(photo)
    val photos = photosDao.getPhotos()
    assertThat(photos.contains(photo)).isTrue()
  }

  @Test
  fun add_and_delete_photo_from_the_database() {
    val photo =
      samplePhotos[0]
    photosDao.insert(photo)
    photosDao.delete(photo)
    val photos = photosDao.getPhotos()
    assertThat(photos.isEmpty()).isTrue()
  }

  @Test
  fun get_all_photos_in_a_particular_album_compare_with_actual_size_and_album_id() {
    val currentAlbumId = 10L
    val currentAlbumPhotos = samplePhotos.filter { photo ->
      photo.albumId == currentAlbumId
    }

    photosDao.insertAll(samplePhotos)

    val photos = photosDao.getPhotosInAlbum(currentAlbumId)

    assertEquals(photos.size, currentAlbumPhotos.size)
    assertEquals(photos.all {
      it.albumId == currentAlbumId
    }, true)
  }

  @Test
  fun delete_photo_by_id_inside_the_database_search_the_element_with_deleted_id_returns_null() {
    val id = 1
    photosDao.insertAll(samplePhotos)
    val photos = photosDao.getPhotos()
    assertEquals(photos.size, 4)

    photosDao.deletePhotoById(id)
    assertEquals(photosDao.getPhotos().size, 3)
    assertEquals(photosDao.getPhotos().find {
      it.uid == id
    }, null)
  }
}