package com.gruppe11.todoApp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gruppe11.todoApp.ui.screens.test1
import com.gruppe11.todoApp.ui.screens.test2
import com.gruppe11.todoApp.ui.screens.test3

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Task.route,
        modifier = modifier
    ) {
        composable(route = Task.route) {
            test1()
        }

        composable(route = Calendar.route) {
            test2()
        }

        composable(route = Settings.route) {
            test3()
        }
    }
}