package com.gruppe11.todoApp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gruppe11.todoApp.ui.screens.CreateTaskPreview
import com.gruppe11.todoApp.ui.screens.EditTaskPaePreview

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CreateTask.route, //TODO: Change CreateTask.route to actual main route
        modifier = modifier
    ) {
        composable(route = CreateTask.route) {
            CreateTaskPreview()
        }

        composable(route = EditTask.route) {
            EditTaskPaePreview()
        }
    }
}