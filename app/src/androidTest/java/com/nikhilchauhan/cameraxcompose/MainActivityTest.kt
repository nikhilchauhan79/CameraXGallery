package com.nikhilchauhan.cameraxcompose

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.nikhilchauhan.cameraxcompose.ui.navigation.NavHostGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {
  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  lateinit var navController: TestNavHostController

  @Before
  fun setUp() {
    hiltRule.inject()
    composeTestRule.setContent {
      navController = TestNavHostController(LocalContext.current)
      navController.navigatorProvider.addNavigator(ComposeNavigator())
      NavHostGraph(
        navController, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
        mockk()
      )
    }
  }

  @Test

  @After
  fun tearDown() {
  }
}