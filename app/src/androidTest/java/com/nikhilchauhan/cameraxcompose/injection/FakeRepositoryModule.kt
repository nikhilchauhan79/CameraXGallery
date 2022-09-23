package com.nikhilchauhan.cameraxcompose.injection

import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {

  @Singleton
  @Provides
  fun providesPhotosRepository(): PhotosRepository = mockk()
}