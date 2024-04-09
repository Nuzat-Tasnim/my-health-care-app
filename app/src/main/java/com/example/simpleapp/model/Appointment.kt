package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName


data class Appointment(
    @SerializedName("_id")
    val id: String,
    @SerializedName("doctor")
    val doctor: String,
    @SerializedName("patient")
    val patient: String,
    @SerializedName("doctorname")
    val doctorName: String,
    @SerializedName("patientname")
    val patientName: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("serial")
    val serial: Int
)

data class AppointmentWithSchedule(
    @SerializedName("date")
    val date: String,
    @SerializedName("doctor")
    val doctor: String,
    @SerializedName("doctorname")
    val doctorname: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("patient")
    val patient: String,
    @SerializedName("patientname")
    val patientname: String,
    @SerializedName("schedule")
    val scheduleTime: ScheduleTime,
    @SerializedName("serial")
    val serial: Int
)

data class AppointmentSetRequest(
    @SerializedName("doctorid")
    val doctorId: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val Month: Int,
    @SerializedName("day")
    val day: Int
)

data class AppointmentDoctorIdRequest(
    @SerializedName("doctorid")
    val doctorId: String
)

data class AppointmentPatientIdRequest(
    @SerializedName("patientid")
    val patientId: String
)

data class AppointmentIdRequest(
    @SerializedName("appointmentid")
    val appointmentId: String
)
