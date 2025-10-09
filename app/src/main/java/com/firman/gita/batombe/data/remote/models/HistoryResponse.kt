package com.firman.gita.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: List<Data>? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName ("fileAudio" )
        val fileAudio: String? = null,

        @SerializedName("pantunBatombe")
        val pantunBatombe: String? = null,

        @SerializedName("userId")
        val userId: Int? = null,

        @SerializedName("isPublic")
        val isPublic: Boolean? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
