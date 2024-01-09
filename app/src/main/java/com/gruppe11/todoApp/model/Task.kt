package com.gruppe11.todoApp.model

import com.gruppe11.todoApp.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Task (
    val id: Int,
    val title: String,
    val priority: Priority,

    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,

    val isCompleted: Boolean,
    val subtasks: List<SubTask>
)


//sealed interface State {
//    data class ContentState(
//        val items: List<String>,
//        val flag: Boolean
//    ) : State
//
//    data object Loading : State
//
//    data class Error(val error: Throwable): State
//}
//
//val state = mutableStateOf<State>(State.Loading)
//
//
//fun ddfd() {
//    val uiState = State.ContentState(
//        items = emptyList(),
//        flag = false
//    )
//    state.value = uiState.copy(flag = true)
//}
//
//
//fun uicode() {
//    when(state.value) {
//        State.Loading -> {
//
//        }
//        is State.ContentState -> {
//
//        }
//
//        else -> {}
//    }
//}
