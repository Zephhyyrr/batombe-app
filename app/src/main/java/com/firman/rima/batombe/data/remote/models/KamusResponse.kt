package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class KamusResponse(
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

        @SerializedName("word")
        val word: String? = null,

        @SerializedName("meaning")
        val meaning: String? = null,

        @SerializedName("audio")
        val audio: String? = null,

        @SerializedName("isDone")
        val isDone: Boolean? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
