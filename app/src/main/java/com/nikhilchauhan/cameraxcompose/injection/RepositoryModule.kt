package com.nikhilchauhan.cameraxcompose.injection

import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Singleton
  @Binds
  abstract fun bindsPhotosRepository(photosRepositoryImpl: PhotosRepositoryImpl): PhotosRepository
}