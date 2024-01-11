package com.gruppe11.todoApp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl("http://10.0.2.2:8080") // DigitalOcean IP: 167.172.171.246
    .build()

object TodoApi {
    val taskService: ITaskApiService by lazy {
        retrofit.create(ITaskApiService::class.java)
    }

    val timeslotService: ITimeslotApiService by lazy {
        retrofit.create(ITimeslotApiService::class.java)
    }

    val subtaskService: ISubtaskApiService by lazy {
        retrofit.create(ISubtaskApiService::class.java)
    }
}