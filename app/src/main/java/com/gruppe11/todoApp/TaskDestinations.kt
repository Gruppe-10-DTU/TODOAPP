package com.gruppe11.todoApp

interface TaskDestination {
    val route: String
}

object CreateTask : TaskDestination {
    override val route = "createTask"
}
object EditTask : TaskDestination {
    override val route = "editTask/{taskId}"
}