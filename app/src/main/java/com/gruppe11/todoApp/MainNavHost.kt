package com.gruppe11.todoApp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gruppe11.todoApp.ui.screens.CalendarScreen
import com.gruppe11.todoApp.ui.screens.CreateTaskContent
import com.gruppe11.todoApp.ui.screens.SettingsPage
import com.gruppe11.todoApp.ui.screens.ShowTaskList
import com.gruppe11.todoApp.ui.screens.test2
import com.gruppe11.todoApp.ui.screens.test3
import com.gruppe11.todoApp.viewModel.CalendarViewModel

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
        // Main destinations
        composable(route = Task.route) {
            ShowTaskList(onFloatingButtonClick = {
                navController.navigate(CreateTask.route)
            })
        }

        composable(route = Calendar.route) {
            test2()
        }

        composable(route = Settings.route) {
            SettingsPage()
        }

        // Task destinations
        composable(route = CreateTask.route) {
            CreateTaskContent(returnPage = {
                navController.popBackStack()
            }, saveTask = { /*TODO*/ })
        }
        composable(route = Calendar.route){
            CalendarScreen(viewModel = CalendarViewModel())
        }
    }
}