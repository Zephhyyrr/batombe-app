package com.firman.rima.batombe.data.repository.meaning

import com.firman.rima.batombe.data.remote.models.MeaningResponse
import com.firman.rima.batombe.data.remote.request.MeaningRequest
import com.firman.rima.batombe.data.remote.service.MeaningService
import com.firman.rima.batombe.utils.ResultState
import javax.inject.Inject

class MeaningRepositoryImpl @Inject constructor(
    private val meaningService: MeaningService
) : MeaningRepository {
    override suspend fun generatePantun(batombe: String): ResultState<MeaningResponse> {
        return try {
            val request = MeaningRequest(batombe)
            val response = meaningService.generateMeaning(request)
            if (response.success) {
                ResultState.Success(response)
            } else {
                ResultState.Error(response.message ?: "Get meaning failed")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error occurred")
        }
    }
}