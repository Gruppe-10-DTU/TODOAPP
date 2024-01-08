package com.gruppe11.todoApp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl("http://10.0.2.2:8080")
    .build()

object TodoApi {
    val taskService: ITaskApiService by lazy {
        retrofit.create(ITaskApiService::class.java)
    }

    val subtaskService: ISubtaskApiService by lazy {
        retrofit.create(ISubtaskApiService::class.java)
    }
}