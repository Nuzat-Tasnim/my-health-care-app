package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentService {
    @POST("/appointments/set")
    fun setAppointment(
        @Header("x-auth-token") token: String,
        @Body appointmentSetRequest: AppointmentSetRequest
    ): Call<Appointment>

    @GET("/appointments/{appointmentid}")
    fun getAppointment(
        @Header("x-auth-token") token: String,
        @Path("appointmentid") appointmentid: String
    ): Call<AppointmentWithSchedule>

    @GET("/appointments/doctor/{doctorid}")
    fun getDoctorAppointments(
        @Header("x-auth-token") token: String,
        @Path("doctorid") doctorid: String
    ): Call<List<AppointmentWithSchedule>>


    @GET("/appointments/patient/{patientid}")
    fun getPatientAppointments(
        @Header("x-auth-token") token: String,
        @Path("patientid") patientid: String
    ): Call<List<AppointmentWithSchedule>>


}