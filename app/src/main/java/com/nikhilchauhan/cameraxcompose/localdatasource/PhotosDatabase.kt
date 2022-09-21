package com.nikhilchauhan.cameraxcompose.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nikhilchauhan.cameraxcompose.localdatasource.entities.Photo
import com.nikhilchauhan.cameraxcompose.constants.AppConstants
import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotosDatabase : RoomDatabase() {

  abstract fun photosDao(): PhotosDao

  companion object {
    @Volatile
    private var photosDatabase: PhotosDatabase? = null

    fun getDatabase(@ApplicationContext context: Context): PhotosDatabase =
      photosDatabase ?: synchronized(this) {
        photosDatabase ?: buildDatabase(context)
      }

    private fun buildDatabase(context: Context): PhotosDatabase =
      Room.databaseBuilder(context, PhotosDatabase::class.java, AppConstants.DB_NAME).build()
  }
}