package com.firman.gita.batombe.data.remote.request

import com.google.gson.annotations.SerializedName

data class HistoryRequest(
    @SerializedName("audioFile")
    val audioFileName: String,

    @SerializedName("pantunBatombe")
    val pantunBatombe: String
)