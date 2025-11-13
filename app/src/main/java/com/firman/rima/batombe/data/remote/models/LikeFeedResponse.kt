package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class LikeFeedResponse(
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

        @SerializedName("like")
        val like: Int? = null,

        @SerializedName("isLiked")
        val isLiked: Boolean? = null
    )
}