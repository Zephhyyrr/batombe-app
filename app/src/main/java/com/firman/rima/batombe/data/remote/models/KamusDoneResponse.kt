package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class KamusDoneResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: Data? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("word")
        val word: String? = null,

        @SerializedName("isDone")
        val isDone: Boolean? = null
    )
}