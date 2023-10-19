package com.gruppe11.todoApp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gruppe11.todoApp.ui.screens.test1
import com.gruppe11.todoApp.ui.screens.test2
import com.gruppe11.todoApp.ui.screens.test3

@Composable
fun BottomNavGraph(navController : NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Task.route
    ) {
        composable(route = BottomBarScreen.Task.route) {
            test1()
        }
        composable(route = BottomBarScreen.Calendar.route) {
            test2()
        }
        composable(route = BottomBarScreen.Settings.route) {
            test3()
        }
    }
}