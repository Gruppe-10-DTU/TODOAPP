package com.gruppe11.todoApp.model

class Tasks(var id: Int, var name: String) {
    @Override
    override fun toString(): String {
        return "$id, $name"
    }

}
