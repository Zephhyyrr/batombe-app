package com.firman.gita.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class UserLogoutResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)
