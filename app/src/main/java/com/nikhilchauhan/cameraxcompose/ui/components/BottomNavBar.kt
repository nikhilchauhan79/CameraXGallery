package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nikhilchauhan.cameraxcompose.ui.states.NavigationItem

@Composable
fun BottomNavBar(
  navController: NavController,
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination
  val items = listOf(NavigationItem.Gallery, NavigationItem.CapturePhoto)

  BottomNavigation {
    items.forEach { navItem ->
      val route = navItem.route
      BottomNavigationItem(selected = currentDestination?.hierarchy?.any { navDestination ->
        navDestination.route ==
          navItem.route
      } == true, onClick = {
        navController.navigate(route) {

          popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
          }
          launchSingleTop = true
          restoreState = true

        }
      }, icon = {
        Column(verticalArrangement = Arrangement.SpaceAround) {
          Icon(
            painter = painterResource(id = navItem.icon), contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
          )
          Text(
            text = stringResource(id = navItem.title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(color = MaterialTheme.colors.onPrimary)
          )
        }
      }, alwaysShowLabel = true, selectedContentColor = MaterialTheme.colors.onPrimary,
        unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.4f)
      )
    }
  }
}