package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName


data class Admin(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userId: String,
    @SerializedName("log")
    val log: List<String>
)

data class AdminCreateRequest(
    @SerializedName("userid")
    val userId: String
)

data class AdminApproveRequest(
    @SerializedName("adminid")
    val adminId: String
)

data class AdminLogEntryRequest(
    @SerializedName("adminid")
    val adminId: String,
    @SerializedName("text")
    val text: String
)

data class AdminLogsRequest(
    @SerializedName("adminid")
    val adminId: String
)
