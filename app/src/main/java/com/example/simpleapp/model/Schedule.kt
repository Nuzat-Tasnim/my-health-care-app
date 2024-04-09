package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName


data class Schedule(
    @SerializedName("_id")
    val id: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("days")
    val days: List<String>,
    @SerializedName("maxAppointment")
    val maxAppointment: Int
)

data class ScheduleTime(
    @SerializedName("from")
    val from: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("to")
    val to: String
)


data class ScheduleCreateRequest(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("days")
    val days: List<String>,
    @SerializedName("maxApppointment")
    val maxAppointment: Int
)
