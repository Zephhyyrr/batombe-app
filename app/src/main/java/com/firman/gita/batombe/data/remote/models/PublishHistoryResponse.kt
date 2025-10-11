package com.firman.gita.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class PublishHistoryResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: Data? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("fileAudio")
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