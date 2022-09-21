package com.nikhilchauhan.cameraxcompose.localdatasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo

@Dao
interface PhotosDao {
  @Query("SELECT * FROM photo")
  fun getPhotos(): List<Photo>

  @Insert
  fun insertAll(list: List<Photo>)

  @Insert
  fun insert(photo: Photo)

  @Delete
  fun delete(photo: Photo)
}