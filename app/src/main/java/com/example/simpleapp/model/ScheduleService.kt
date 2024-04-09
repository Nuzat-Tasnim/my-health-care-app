package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    @POST("/schedules/create")
    fun createSchedule(
        @Body scheduleCreateRequest: ScheduleCreateRequest
    ): Call<Schedule>

    @GET("/schedules/{scheduleid}")
    fun getSchedule(
        @Path("scheduleid") scheduleid: String
    ): Call<Schedule>

    @DELETE("/schedules/remove/{scheduleid}")
    fun deleteSchedule(
        @Path("scheduleid") scheduleid: String
    )
}