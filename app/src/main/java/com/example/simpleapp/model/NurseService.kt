package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NurseService {

    @POST("/nurses/search")
    fun createNurse(
        @Body searchRequest: NurseSearchRequest
    ): Call<List<Nurse>>

    @GET("/nurses/{nurseid}")
    fun getNurse(
        @Header("x-auth-token") token: String,
        @Path("nurseid") nurseid: String
    ): Call<Nurse>
    
    @GET("/nurses/unapproved")
    fun getUnapprovedNurses(
        @Header("x-auth-token") token: String
    ): Call<List<Nurse>>

    @PUT("/nurses/approve")
    fun approveNurse(
        @Header("x-auth-token") token: String,
        @Body nurseApproveRequest: NurseApproveRequest
    ): Call<Nurse>

    @POST("/nurses/assignPatient")
    fun assignPatientNurse(
        @Header("x-auth-token") token: String,
        @Body nurseAssignPatientRequest: NurseAssignPatientRequest
    ): Call<Patient>

    @GET("/nurses/assignedToMe")
    fun assignedToNurse(
        @Header("x-auth-token") token: String,
    ): Call<List<Nurse>>

    @POST("/nurses/create")
    fun createNurse(
        @Header("x-auth-token") token: String,
        @Body nurseCreateRequest: NurseCreateRequest
    )
}