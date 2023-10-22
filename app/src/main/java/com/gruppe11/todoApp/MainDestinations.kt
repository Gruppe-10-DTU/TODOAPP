package com.gruppe11.todoApp

interface MainDestination {
    val route: String
}

object CreateTask : MainDestination {
    override val route = "createTask"
}

object EditTask : MainDestination {
    override val route = "editTask"
}

val mainTabRowScreens = listOf(CreateTask, EditTask)