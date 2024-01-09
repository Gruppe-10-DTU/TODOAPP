package com.gruppe11.todoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class SubTask(
    var title: String,
    val id: Int,
    val completed: Boolean = false
)







