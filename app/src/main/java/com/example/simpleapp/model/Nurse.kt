package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class Nurse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userId: String,
    @SerializedName("assignedTo")
    val assignedTo: List<String>,
    @SerializedName("approvedBy")
    val approvedBy: String
)

data class NurseAssignPatientRequest(
    @SerializedName("nurseid")
    val nurseId: String,
    @SerializedName("patientid")
    val patientId: String
)

data class NurseCreateRequest(
    @SerializedName("userid")
    val userId: String
)

data class NurseApproveRequest(
    @SerializedName("nurseid")
    val nurseId: String
)

data class NurseSearchRequest(
    @SerializedName("name")
    val name: String
)

