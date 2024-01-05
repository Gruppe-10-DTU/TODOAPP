package com.gruppe11.todoApp.network

import com.gruppe11.todoApp.model.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ITaskApiService {

    @POST("/tasks")
    suspend fun createTask(@Body task: Task): Task

    @GET("/tasks/{id}")
    suspend fun read(@Path("id") id: Int): Task?

    @GET("/tasks")
    suspend fun readAll(): List<Task>

    @PUT("/tasks/{id}")
    suspend fun update(@Path("id") id: Int, @Body task: Task): Task

    @DELETE("/tasks/{id}")
    suspend fun delete(@Path("id") id: Int)
}