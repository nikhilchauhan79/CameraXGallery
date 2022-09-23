package com.nikhilchauhan.cameraxcompose

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nikhilchauhan.cameraxcompose.localdatasource.PhotosDatabase
import com.nikhilchauhan.cameraxcompose.localdatasource.dao.PhotosDao
import com.nikhilchauhan.cameraxcompose.repository.PhotosRepository
import com.nikhilchauhan.cameraxcompose.ui.GalleryVM
import com.nikhilchauhan.cameraxcompose.ui.navigation.NavHostGraph
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {
  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  lateinit var navController: TestNavHostController

  @OptIn(ExperimentalCoroutinesApi::class)
  private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

  @Inject
  lateinit var photosRepository: PhotosRepository

  @BindValue
  val galleryVM: GalleryVM = mockk(relaxed = true)

  private lateinit var photosDao: PhotosDao

  private lateinit var photosDatabase: PhotosDatabase

  @OptIn(ExperimentalCoroutinesApi::class)
  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    hiltRule.inject()
    MockKAnnotations.init()

    val context = ApplicationProvider.getApplicationContext<Context>()
    photosDatabase = Room.inMemoryDatabaseBuilder(context, PhotosDatabase::class.java).build()
    photosDao = photosDatabase.photosDao()
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      NavHostGraph(
        navController, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
        mockk()
      )
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun nav_test() = runTest {
    coEvery {
      photosRepository.getPhotos()
    } returns listOf()
    composeTestRule.onNodeWithText("Photos").assertIsDisplayed()
  }

  @After
  @Throws(IOException::class)
  fun tearDown() {
    photosDatabase.close()
    Dispatchers.resetMain()
  }
}