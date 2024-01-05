package com.gruppe11.todoApp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val apiUrl = "http://10.0.2.2:8080"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(apiUrl)
    .build()

object TodoApi {
    val taskServiceImpl: ITaskApiService by lazy {
        retrofit.create(ITaskApiService::class.java)
    }

    val subtaskServiceImpl: ISubtaskApiService by lazy {
        retrofit.create(ISubtaskApiService::class.java)
    }
}