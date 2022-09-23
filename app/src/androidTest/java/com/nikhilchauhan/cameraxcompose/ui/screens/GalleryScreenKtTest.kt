package com.nikhilchauhan.cameraxcompose.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import com.nikhilchauhan.cameraxcompose.localdatasource.PhotosDatabaseTest
import org.junit.Rule
import org.junit.Test

class GalleryScreenKtTest {
  @get:Rule(order = 0)
  val composeTestRule = createComposeRule()

  lateinit var navController: TestNavHostController

  @Test
  fun test_if_the_number_of_albumsDisplayed_on_the_screen_is_2() {
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      PhotosGrid(
        photos = PhotosDatabaseTest.samplePhotos, navController = navController, onPhotoClick = {
      })
    }
    composeTestRule.onAllNodes(hasContentDescription("Album Name"), useUnmergedTree = true)
      .assertCountEquals(2)
  }

  @Test
  fun test_if_the_number_of_photosDisplayed_on_the_screen_is_4() {
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      PhotosGrid(
        photos = PhotosDatabaseTest.samplePhotos, navController = navController, onPhotoClick = {
      })
    }
    composeTestRule.onAllNodes(hasContentDescription("Image"), useUnmergedTree = true)
      .assertCountEquals(4)
  }
}