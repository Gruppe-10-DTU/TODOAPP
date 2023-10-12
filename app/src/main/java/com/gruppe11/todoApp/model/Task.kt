package com.gruppe11.todoApp.model

class Task(
    var id: Int,
    var name: String,
    var date: Int
)
{
    @Override
    override fun toString(): String {
        return "$id, $name, $date"
    }
}
