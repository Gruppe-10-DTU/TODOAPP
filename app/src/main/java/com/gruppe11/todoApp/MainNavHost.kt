package com.gruppe11.todoApp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gruppe11.todoApp.ui.screens.CalendarScreen
import com.gruppe11.todoApp.ui.screens.CreateTaskContent
import com.gruppe11.todoApp.ui.screens.ManageProfileScreen
import com.gruppe11.todoApp.ui.screens.ManageTimeSlotsScreen
import com.gruppe11.todoApp.ui.screens.SchedulingScreen
import com.gruppe11.todoApp.ui.screens.SettingsPage
import com.gruppe11.todoApp.ui.screens.ShowTaskList

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
            ShowTaskList(
                onFloatingButtonClick = {navController.navigate(CreateTask.route)},
                onEditTask = { navController.navigate(route = EditTask.route.replace("{taskId}", it.toString())) }
            )
        }

        composable(route = Calendar.route) {
            // Creates a ViewModel from the current BackStackEntry
            // Available in the androidx.hilt:hilt-navigation-compose artifact
            CalendarScreen()
        }

        composable(route = Settings.route) {
            SettingsPage(
                manageTimeSlot = {navController.navigate(ManageTimeSlots.route)},
                manageProfile = {navController.navigate(ManageProfile.route)}
            )
        }

        // Task destinations
        composable(route = CreateTask.route) {
            CreateTaskContent(returnPage = {
                navController.popBackStack()
            })
        }
        composable(route = Scheduler.route){
            SchedulingScreen()
        }
        composable(route = ManageTimeSlots.route){
            ManageTimeSlotsScreen(returnPage = {navController.popBackStack()})
        }
        composable(route = ManageProfile.route){
            ManageProfileScreen(returnPage = {navController.popBackStack()})
        }
        composable(
            route = EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) {
//            EditTaskScreen(
//                returnPage = { navController.popBackStack() },
//                taskId = it.arguments?.getInt("taskId")!!
//            )
            CreateTaskContent(
                returnPage = { navController.popBackStack() },
                taskId = it.arguments?.getInt("taskId")
            )
        }
    }
}