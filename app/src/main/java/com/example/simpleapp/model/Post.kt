package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName


data class Post(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("bloodtype")
    val bloodType: String,
    @SerializedName("text")
    val text: String?,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("date")
    val date: String
)

data class PostSendRequest(
    @SerializedName("bloodtype")
    val bloodType: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("contact")
    val contact: String
)
