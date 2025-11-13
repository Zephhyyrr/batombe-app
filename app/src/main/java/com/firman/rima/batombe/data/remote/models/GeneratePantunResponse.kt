package com.firman.rima.batombe.data.remote.models

import com.google.gson.annotations.SerializedName

data class GeneratePantunResponse (
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: Data? = null
){
        data class Data(
            @SerializedName("pantun_batombe")
            val pantun: String? = null
        )
    }