package com.firman.gita.batombe.data.remote.request

import com.google.gson.annotations.SerializedName

data class GeneratePantunRequest(
    @SerializedName("jumlah_baris")
    val jumlah_baris: Int,

    @SerializedName("tema")
    val tema: String,

    @SerializedName("emosi")
    val emosi: String
)