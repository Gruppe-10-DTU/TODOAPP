package com.gruppe11.todoApp.ui.elements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Tag(
    val id: Int,
    val label: String
) {
    var checked by mutableStateOf(false)
}
