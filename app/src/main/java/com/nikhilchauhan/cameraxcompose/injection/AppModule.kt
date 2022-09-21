package com.nikhilchauhan.cameraxcompose.injection

import android.content.Context
import com.nikhilchauhan.cameraxcompose.localdatasource.PhotosDatabase
import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun providePhotosDB(@ApplicationContext context: Context): PhotosDatabase =
    PhotosDatabase.getDatabase(context)

  @Singleton
  @Provides
  fun providePhotosDao(photosDatabase: PhotosDatabase): PhotosDao = photosDatabase.photosDao()
}