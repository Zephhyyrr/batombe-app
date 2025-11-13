package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class ExampleVideoResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: List<Data>? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("url")
        val url: String? = null,

        @SerializedName("imageUrl")
        val imageUrl: String? = null,

        @SerializedName("createdAt")
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}
