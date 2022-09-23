package com.nikhilchauhan.cameraxcompose.injection

import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [RepositoryModule::class]
)
abstract class FakeRepositoryModule {

  @Singleton
  @Binds
  abstract fun bindsPhotosRepository(fakePhotosRepository: PhotosRepository = mockk()): PhotosRepository
}