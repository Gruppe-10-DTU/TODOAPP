package com.gruppe11.todoApp.model

import android.annotation.SuppressLint
import java.time.LocalDateTime

class Task() {
    var id: Int = 0
    var title: String = ""
    var priority: Priority? = null
    var completion: LocalDateTime? = null
    var isCompleted = false

    constructor(id: Int, title: String, completion: LocalDateTime, priority: String, isCompleted: Boolean) : this() {
        this.id = id
        this.title = title
        this.priority = Priority.valueOf(priority)
        this.completion = completion
        this.isCompleted = isCompleted
    }

    @SuppressLint("NewApi")
    override fun toString(): String {
        return "Task: $id,\n title: $title,\n priority: $priority,\n complete by: ${completion!!.dayOfMonth},\n isCompleted: $isCompleted"
    }


}