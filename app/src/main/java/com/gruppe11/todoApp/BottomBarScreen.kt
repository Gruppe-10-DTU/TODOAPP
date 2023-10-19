package com.gruppe11.todoApp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Calendar : BottomBarScreen(
        route = "calendar",
        title = "Calendar",
        icon = Icons.Default.CalendarMonth
    )
    object Task : BottomBarScreen(
        route = "task",
        title = "Task",
        icon = Icons.Default.CheckCircleOutline
    )
    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}