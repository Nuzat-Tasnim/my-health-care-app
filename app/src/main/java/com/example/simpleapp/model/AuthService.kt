package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequestBody): Call<User>

    @POST("/auth/register")
    fun register(@Body registerRequestBody: RegisterRequestBody): Call<User>
}