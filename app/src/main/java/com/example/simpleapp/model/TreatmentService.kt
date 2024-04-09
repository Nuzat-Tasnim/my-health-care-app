package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body


interface TreatmentService {

    @GET("/treatment/{treatmentid}")
    fun getTreatment(
        @Header("x-auth-token") token: String,
        @Path("treatmentid") treatmentid: String
    ): Call<Treatment>

    @GET("/treatments/patient/{patientid}")
    fun getTreatmentsOfPatient(
        @Header("x-auth-token") token: String,
        @Path("patientid") patientid: String
    ): Call<List<Treatment>>

    @POST("/treatments/create")
    fun createTreatement(
        @Header("x-auth-token") token: String,
        @Body treatmentBody: TreatmentCreateRequest
    ): Call<Treatment>

}