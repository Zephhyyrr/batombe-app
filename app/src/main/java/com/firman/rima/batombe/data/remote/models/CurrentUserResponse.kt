package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName


data class CurrentUserResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: Data? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("email")
        val email: String? = null,

        @SerializedName("profileImage")
        val profileImage: Any? = null,

        @SerializedName("isDatuak")
        val isDatuak: Boolean? = false
    )
}