package com.example.simpleapp.model

import com.google.gson.annotations.SerializedName

data class ApplyRequest(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userid: String,
    @SerializedName("appliedAs")
    val appliedAs: String,
    @SerializedName("initialData")
    val initialData: String
)
data class ApplyRequestWithName(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userid")
    val userInfo: UserInfo,
    @SerializedName("appliedAs")
    val appliedAs: String,
    @SerializedName("initialData")
    val initialData: String
)

data class NewRequestBody(
    @SerializedName("userid")
    val userid: String,
    @SerializedName("appliedAs")
    val appliedAs: String,
    @SerializedName("initialData")
    val initialData: String
)
data class ApproveRequestBody(
    @SerializedName("applyRequestId")
    val applyRequestId: String,
    @SerializedName("adminid")
    val adminid: String
)
data class FindRequestBody(
    @SerializedName("appliedAs")
    val appliedAs: String
)

