package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("/users/all")
    fun getUsers(): Call<List<User>>

    @GET("/users/{userid}")
    fun getUser(
        @Header("x-auth-token") token: String,
        @Path("userid") userId: String
    ): Call<User>

    @POST("/users/search")
    fun searchUser(
        @Header("x-auth-token") token: String,
        @Body name: SearchUserRequestBody
    ): Call<List<User>>

    @POST("/users/editUser/{userid}")
    fun editProfile(
        @Header("x-auth-token") token: String,
        @Path("userid") userId: String,
        @Body user: EditUserRequestBody
    ): Call<User>

    @DELETE("/users/removeuser/{userid}")
    fun deleteUser(
        @Header("x-auth-token") token: String,
        @Path("userid") userId: String,
    )
}