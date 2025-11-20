package com.firman.rima.batombe.data.repository.meaning

import com.firman.rima.batombe.data.remote.models.MeaningResponse
import com.firman.rima.batombe.utils.ResultState

interface MeaningRepository {
    suspend fun generatePantun(batombe: String): ResultState<MeaningResponse>
}