package com.gruppe11.todoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class SubTask(
    val title: String,
    val id: Int,
    val completed: Boolean = false
)







