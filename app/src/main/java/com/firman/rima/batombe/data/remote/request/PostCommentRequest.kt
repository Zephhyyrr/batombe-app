package com.firman.rima.batombe.data.remote.request

import com.google.gson.annotations.SerializedName

data class PostCommentRequest(
    @SerializedName("content")
    val content: String,

    @SerializedName("historyId")
    val historyId: Int
)