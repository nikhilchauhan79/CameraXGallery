package com.nikhilchauhan.cameraxcompose.injection

import com.nikhilchauhan.cameraxcompose.localdatasource.PhotosDatabase
import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class], replaces = [AppModule::class]
)
object FakeAppModule {

  @Singleton
  @Provides
  fun providePhotosDB(): PhotosDatabase =
    mockk()

  @Singleton
  @Provides
  fun providePhotosDao(photosDatabase: PhotosDatabase): PhotosDao = photosDatabase.photosDao()
}