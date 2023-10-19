package com.gruppe11.todoApp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {}
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController : NavHostController) {
    val screens = listOf(
        BottomBarScreen.Task,
        BottomBarScreen.Settings,
        BottomBarScreen.Calendar
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        screens.forEach{ screen ->

        }
    }
}

@Composable
fun RowScope.AddItem (
    screen: BottomBarScreen,
    currentDestination: NavDestination,
    navController: NavHostController
) {
    //BottomNavigationItem()

}