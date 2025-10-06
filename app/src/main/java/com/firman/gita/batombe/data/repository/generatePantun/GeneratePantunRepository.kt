package com.firman.gita.batombe.data.repository.generatePantun

import com.firman.gita.batombe.data.remote.models.GeneratePantunResponse
import com.firman.gita.batombe.utils.ResultState

interface GeneratePantunRepository {
    suspend fun generatePantun(jumlah_baris: Int, tema: String, emosi: String): ResultState<GeneratePantunResponse>
}