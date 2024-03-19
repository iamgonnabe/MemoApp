package com.example.memoapp.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.memoapp.R
import com.example.memoapp.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavController){
    val items = listOf(
        BottomNavItem.NewFolderScreen,
        BottomNavItem.MemoScreen
    )
    BottomNavigation(
        backgroundColor = Color.Black,
        contentColor = colorResource(id = R.color.contentColor)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            BottomNavigationItem(
                modifier = Modifier.padding(end= 100.dp, bottom = 30.dp),
                icon = { Icon(painterResource(id = items[0].icon), contentDescription = null, modifier = Modifier.size(30.dp)) },
                selected = currentDestination?.hierarchy?.any { it.route == items[0].route } == true,
                onClick = {
                    navController.navigate(items[0].route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                modifier = Modifier.padding(start= 100.dp, bottom = 30.dp),
                icon = { Icon(painterResource(id = items[1].icon), contentDescription = null, modifier = Modifier.size(30.dp)) },
                selected = currentDestination?.hierarchy?.any { it.route == items[1].route } == true,
                onClick = {
                    navController.navigate(items[1].route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}