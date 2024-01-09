package com.gruppe11.todoApp

interface SettingsDestination {
    val route: String
}
object ManageTimeSlots: SettingsDestination {
    override val route: String = "manageTimeSlots"
}