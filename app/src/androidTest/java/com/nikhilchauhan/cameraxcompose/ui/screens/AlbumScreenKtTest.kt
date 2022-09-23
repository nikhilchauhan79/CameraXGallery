package com.nikhilchauhan.cameraxcompose.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.testing.TestNavHostController
import com.nikhilchauhan.cameraxcompose.localdatasource.PhotosDatabaseTest
import org.junit.Rule
import org.junit.Test

class AlbumScreenKtTest {
  @get:Rule(order = 0)
  val composeTestRule = createComposeRule()

  private lateinit var navController: TestNavHostController

  @Test
  fun test_if_1_image_is_displayed_at_the_album_screen() {
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      AlbumScreen(
        photosList = PhotosDatabaseTest.samplePhotos
      )
    }

    composeTestRule.onNodeWithContentDescription("Album Image_1", useUnmergedTree = true)
      .assertIsDisplayed()
  }

  @Test
  fun test_if_one_selected_dot_indicator_is_displayed() {
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      AlbumScreen(
        photosList = PhotosDatabaseTest.samplePhotos
      )
    }

    composeTestRule.onNodeWithContentDescription("SelectedDot", useUnmergedTree = true)
      .assertIsDisplayed()
  }

  @Test
  fun test_if_3_unselected_dot_indicators_are_displayed() {
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      AlbumScreen(
        photosList = PhotosDatabaseTest.samplePhotos
      )
    }

    composeTestRule.onAllNodesWithContentDescription("UnSelectedDot", useUnmergedTree = true)
      .assertCountEquals(3)
  }
}