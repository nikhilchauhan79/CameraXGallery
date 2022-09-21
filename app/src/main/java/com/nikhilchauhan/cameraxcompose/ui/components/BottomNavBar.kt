package com.nikhilchauhan.cameraxcompose.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
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
        Icon(painter = painterResource(id = navItem.icon), contentDescription = null)
      }, alwaysShowLabel = true
      )
    }
  }
}