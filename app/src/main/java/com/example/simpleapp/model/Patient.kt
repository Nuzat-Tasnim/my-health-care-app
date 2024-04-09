package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userId: String,
    @SerializedName("bloodtype")
    val bloodType: String,
    @SerializedName("allergies")
    val allergies: String,
    @SerializedName("medicalHistories")
    val medicalHistories: String,
    @SerializedName("treatments")
    val treatments: List<String>,
    @SerializedName("myDoctors")
    val myDoctors: List<String>
)
data class PatientUpdateRequest(
    @SerializedName("bloodtype")
    val bloodType: String,
    @SerializedName("allergies")
    val allergies: List<String>,
    @SerializedName("medicalHistories")
    val medicalHistories: List<String>
)

data class PatientCreateRequest(
    @SerializedName("userid")
    val userId: String
)

data class PatientAddTreatmentRequest(
    @SerializedName("patientid")
    val patientId: String,
    @SerializedName("treatmentid")
    val treatmentId: String
)

data class PatientIdRequest(
    @SerializedName("patientid")
    val patientId: String
)

