package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

interface ApplyRequestService {
    @POST("/applyRequests/newRequest")
    fun newRequest(
        @Header("x-auth-token") token: String,
        @Body newRequestBody: NewRequestBody
    ): Call<Any>

    @POST("/applyRequests/approve")
    fun approveRequest(
        @Header("x-auth-token") token: String,
        @Body approveRequestBody: ApproveRequestBody
    ): Call<Any>

    @POST("/applyRequests/find")
    fun findRequest(
        @Header("x-auth-token") token: String,
        @Body findRequestBody: FindRequestBody
    ): Call<List<ApplyRequestWithName>>

    @POST("/applyRequests/reject")
    fun rejectRequest(
        @Header("x-auth-token") token: String,
        @Body approveRequestBody: ApproveRequestBody
    ): Call<Any>
}