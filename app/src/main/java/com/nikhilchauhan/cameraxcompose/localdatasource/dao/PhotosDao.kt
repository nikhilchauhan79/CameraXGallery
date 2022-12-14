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

  @Query("SELECT * FROM photo WHERE album_id = :albumId")
  fun getPhotosInAlbum(albumId: Long): List<Photo>

  @Query("SELECT MAX(total_albums) FROM photo")
  fun getTotalAlbums(): Int

  @Insert
  fun insertAll(list: List<Photo>)

  @Insert
  fun insert(photo: Photo)

  @Delete
  fun delete(photo: Photo)

  @Query("DELETE FROM photo WHERE uid = :id")
  fun deletePhotoById(id: Int)
}