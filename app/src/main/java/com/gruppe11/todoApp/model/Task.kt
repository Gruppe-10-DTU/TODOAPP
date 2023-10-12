package com.gruppe11.todoApp.model

import java.time.LocalDateTime

class Task() {
    var id: Int = 0
    var title: String? = null
    var priority: Priority? = null
    var completion: LocalDateTime? = null
    var isCompleted = false
}