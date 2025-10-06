package com.firman.gita.batombe.data.remote.request

import com.google.gson.annotations.SerializedName

data class AnalyzeRequest(
    @SerializedName("text")
    val text: String,
    @SerializedName("audioFileName")
    val audioFileName: String
)
