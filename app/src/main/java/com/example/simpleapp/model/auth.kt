package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequestBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val Month: Int,
    @SerializedName("day")
    val day: Int,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

