package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientService {
    @PUT("/patients/update/{patientid}")
    fun updatePatients(
        @Header("x-auth-token") token: String,
        @Path("patientid") patientid: String,
        @Body patientUpdateRequest: PatientUpdateRequest
    ): Call<Patient>

    @POST("/patients/create")
    fun createPatient(
        @Header("x-auth-token") token: String,
        @Body patientCreateRequest: PatientCreateRequest
    ): Call<Patient>

    @POST("/patients/addTreatment")
    fun addTreatment(
        @Header("x-auth-token") token: String,
        @Body patientAddTreatmentRequest: PatientAddTreatmentRequest
    ): Call<Patient>

    @GET("/patients/{patientid}")
    fun getPatient(
        @Header("x-auth-token") token: String,
        @Path("patientid") patientid: String,
    ): Call<Patient>

}