package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName


data class Review(
    @SerializedName("_id")
    val id: String,
    @SerializedName("patient")
    val patient: String,
    @SerializedName("doctor")
    val doctor: String,
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("text")
    val text: String
)