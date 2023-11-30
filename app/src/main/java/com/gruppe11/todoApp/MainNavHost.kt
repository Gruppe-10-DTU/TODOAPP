package com.gruppe11.todoApp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gruppe11.todoApp.ui.screenStates.CalendarScreenState
import com.gruppe11.todoApp.ui.screens.CalendarScreen
import com.gruppe11.todoApp.ui.screens.CreateTaskContent
import com.gruppe11.todoApp.ui.screens.EditTaskScreen
import com.gruppe11.todoApp.ui.screens.SettingsPage
import com.gruppe11.todoApp.ui.screens.ShowTaskList
import com.gruppe11.todoApp.viewModel.CalendarViewModel

@SuppressLint("NewApi")
@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val calendarViewModel = remember { CalendarViewModel(CalendarScreenState()) }

    NavHost(
        navController = navController,
        startDestination = Task.route,
        modifier = modifier
    ) {
        // Main destinations
        composable(route = Task.route) {
            ShowTaskList(
                onFloatingButtonClick = {navController.navigate(CreateTask.route)},
                onEditTask = { navController.navigate(route = EditTask.route.replace("{taskId}", it.toString())) }
            )
        }

        composable(route = Calendar.route) {
            CalendarScreen(calendarViewModel)
        }

        composable(route = Settings.route) {
            SettingsPage()
        }

        // Task destinations
        composable(route = CreateTask.route) {
            CreateTaskContent(returnPage = {
                navController.popBackStack()
            })
        }
        composable(
            route = EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) {
            EditTaskScreen(
                returnPage = { navController.popBackStack() },
                taskId = it.arguments?.getInt("taskId")!!
            )
        }
    }
}