package com.gruppe11.todoApp.model

enum class Priority {
    LOW, MEDIUM, HIGH;
}
fun fromString(S: String) : Priority {
    return Priority.valueOf(S);
}