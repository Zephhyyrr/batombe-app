package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class MeaningResponse (
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: Data? = null
){
    data class Data(
        @SerializedName("makna_batombe")
        val makna_batombe: String? = null
    )
}