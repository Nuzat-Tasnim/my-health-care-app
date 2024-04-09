package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminService {
    @POST("/admins/create")
    fun createAdmin(
        @Header("x-auth-token") token: String,
        @Body adminCreateRequest: AdminCreateRequest
    ): Call<Admin>

    @GET("/admins/{adminid}")
    fun getAdmin(
        @Header("x-auth-token") token: String,
        @Path("adminid") adminid: String
    ): Call<Admin>

    @GET("/admins/unapproved")
    fun unapprovedAdmins(
        @Header("x-auth-token") token: String,
    ): Call<List<Admin>>

    @PUT("/admins/approve")
    fun approveAdmin(
        @Header("x-auth-token") token: String,
        @Body adminApproveRequest: AdminApproveRequest
    ): Call<Admin>

    @POST("/admins/logentry")
    fun adminLogEntry(
        @Body adminLogEntryRequest: AdminLogEntryRequest
    ): Call<Admin>

    @POST("/admins/logs/{adminid}")
    fun adminLogs(
        @Path("adminid") adminid: String
    ): Call<Admin>
}