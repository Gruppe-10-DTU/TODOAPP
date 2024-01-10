package com.gruppe11.todoApp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

interface MainDestination {
    val route: String
    val title: String
    val icon: ImageVector
}

object Task : MainDestination {
    override val route = "task"
    override val title = "Task"
    override val icon = Icons.Default.CheckCircleOutline
}
object Settings : MainDestination {
    override val route = "settings"
    override val title = "Settings"
    override val icon = Icons.Default.Settings
}
object Calendar : MainDestination {
    override val route = "calendar"
    override val title = "Calendar"
    override val icon = Icons.Default.CalendarMonth
}
object Scheduler: MainDestination {
    override val route = "schedule"
    override val title = "Schedule"
    override val icon = Icons.Default.Schedule
}