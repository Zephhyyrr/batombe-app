package com.firman.rima.batombe.data.repository.generatePantun

import com.firman.rima.batombe.data.remote.models.GeneratePantunResponse
import com.firman.rima.batombe.utils.ResultState

interface GeneratePantunRepository {
    suspend fun generatePantun(jumlah_baris: Int, tema: String, emosi: String): ResultState<GeneratePantunResponse>
}