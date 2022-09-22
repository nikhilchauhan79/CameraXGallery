package com.nikhilchauhan.cameraxcompose.localdatasource.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class Photo(
  @PrimaryKey(autoGenerate = true)
  val uid: Int,
  @ColumnInfo(name = "name") val name: String?,
  @ColumnInfo(name = "time_stamp") val timeStamp: Long?,
  @ColumnInfo(name = "album_name") val albumName: String?,
  @ColumnInfo(name = "album_id") val albumId: Long?,
  @ColumnInfo(name = "path") val path: String?,
  @ColumnInfo(name = "total_photos") val totalPhotos: Int?,
  @ColumnInfo(name = "is_session_first") val isSessionFirst: Boolean = false
)
