package com.firman.gita.batombe.data.repository.generatePantun

import com.firman.gita.batombe.data.remote.models.GeneratePantunResponse
import com.firman.gita.batombe.data.remote.request.GeneratePantunRequest
import com.firman.gita.batombe.data.remote.service.GeneratePantunService
import com.firman.gita.batombe.utils.ResultState
import javax.inject.Inject

class GeneratePantunRepositoryImpl @Inject constructor (
    private val generatePantunService: GeneratePantunService
): GeneratePantunRepository{
    override suspend fun generatePantun(
        jumlah_baris: Int,
        tema: String,
        emosi: String
    ): ResultState<GeneratePantunResponse> {
        return try {
            val request = GeneratePantunRequest(jumlah_baris, tema, emosi)
            val response = generatePantunService.generatePantun(request)
            if (response.success){
                ResultState.Success(response)
            } else {
                ResultState.Error(response.message ?: "Generate Pantun failed")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error occurred")
        }
    }
}