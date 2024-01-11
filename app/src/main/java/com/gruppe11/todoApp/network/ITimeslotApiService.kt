package com.gruppe11.todoApp.network

import com.gruppe11.todoApp.model.TimeSlot
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ITimeslotApiService {

    @POST("/timeslots")
    suspend fun createTimeslots(@Body timeslot: TimeSlot): TimeSlot

    @GET("/timeslots/{id}")
    suspend fun read(@Path("id") id: Int): TimeSlot?

    @GET("/timeslots")
    suspend fun readAll(): List<TimeSlot>

    @PUT("/timeslots/{id}")
    suspend fun update(@Path("id") id: Int, @Body timeSlot: TimeSlot): TimeSlot

    @DELETE("/timeslots/{id}")
    suspend fun delete(@Path("id") id: Int)
}