package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("birthdate")
    val birthdate: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("adminid")
    val adminid: String,
    @SerializedName("doctorid")
    val doctorid: String,
    @SerializedName("nurseid")
    val nurseid: String,
    @SerializedName("patientid")
    val patientid: String,
    @SerializedName("roles")
    val roles: List<String>
)

data class EditUserRequestBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val Month: Int,
    @SerializedName("day")
    val day: Int,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("gender")
    val gender: String
)

data class SearchUserRequestBody(
    @SerializedName("name")
    val name: String
)

