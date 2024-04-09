package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userId: String,
    @SerializedName("areaOfExpertise")
    val areaOfExpertise: String,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("schedule")
    val schedule: String,
    @SerializedName("approvedBy")
    val approvedBy: String
)
data class UserInfo(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class DoctorNameClass(
    @SerializedName("approvedBy")
    val approvedBy: String,
    @SerializedName("areaOfExpertise")
    val areaOfExpertise: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("rate")
    val rate: Int,
    @SerializedName("schedule")
    val schedule: String,
    @SerializedName("userid")
    val userInfo: UserInfo,
    @SerializedName("__v")
    val v: Int
)


data class DoctorSearchRequest(
    @SerializedName("name")
    val name: String
)

data class DoctorDepartmentRequest(
    @SerializedName("areaOfExpertise")
    val areaOfExpertise: String
)

data class DoctorCreateRequest(
    @SerializedName("userid")
    val userId: String,
    @SerializedName("areaOfExpertise")
    val areaOfExpertise: String
)

data class DoctorEditRequest(
    @SerializedName("doctorid")
    val doctorid: String,
    @SerializedName("areaOfExpertise")
    val areaOfExpertise: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("days")
    val days: List<String>,
    @SerializedName("maxApppointment")
    val maxAppointment: Int
)

data class DoctorRateRequest(
    @SerializedName("patientid")
    val patientId: String,
    @SerializedName("rate")
    val rate: Int,
    @SerializedName("text")
    val text: String
)

data class DoctorAddScheduleRequest(
    @SerializedName("doctorid")
    val doctorId: String,
    @SerializedName("scheduleid")
    val scheduleId: String
)

data class DoctorApproveRequest(
    @SerializedName("doctorid")
    val doctorId: String
)

