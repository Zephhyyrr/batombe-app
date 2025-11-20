package com.firman.rima.batombe.data.remote.request

import com.google.gson.annotations.SerializedName

data class MeaningRequest(
    @SerializedName("batombe")
    val batombe: String,
)