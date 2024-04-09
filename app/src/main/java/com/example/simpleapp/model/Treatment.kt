package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class Treatment(
    @SerializedName("_id")
    val id: String,
    @SerializedName("symptom")
    val symptom: String,
    @SerializedName("assessment")
    val assessment: String,
    @SerializedName("prescription")
    val prescription: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("pressureHigh")
    val pressureHigh: Double,
    @SerializedName("pressureLow")
    val pressureLow: Double,
    @SerializedName("sugarLevel")
    val sugarLevel: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("nurseAssigned")
    val nurseAssigned: List<String>
)

data class TreatmentCreateRequest(
    @SerializedName("symptom")
    val symptom: String,
    @SerializedName("assessment")
    val assessment: String,
    @SerializedName("prescription")
    val prescription: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("pressureHigh")
    val pressureHigh: Double,
    @SerializedName("pressureLow")
    val pressureLow: Double,
    @SerializedName("sugarLevel")
    val sugarLevel: Double
)

data class TreatmentIdRequest(
    @SerializedName("treatmentid")
    val treatmentId: String
)
