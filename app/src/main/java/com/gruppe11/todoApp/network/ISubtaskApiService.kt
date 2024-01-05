package com.gruppe11.todoApp.network

import com.gruppe11.todoApp.model.SubTask
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ISubtaskApiService {

    @GET("/tasks/{id}/subtasks")
    suspend fun readAll(@Path("id") id: Int): List<SubTask>

    @POST("/tasks/{id}/subtasks")
    suspend fun createSubtask(@Path("id") id: Int, @Body subtask: SubTask): SubTask

    @PUT("/tasks/{id}/subtasks/{subtaskId}")
    suspend fun update(@Path("id") id: Int, @Path("subtaskId") subtaskId: Int, @Body subtask: SubTask): SubTask?

    @DELETE("/tasks/{id}/subtasks/{subtaskId}")
    suspend fun delete(@Path("id") id: Int, @Path("subtaskId") subtaskId: Int)
}