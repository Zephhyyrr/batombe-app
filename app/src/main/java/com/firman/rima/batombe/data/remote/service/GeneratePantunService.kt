package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.GeneratePantunResponse
import com.firman.rima.batombe.data.remote.request.GeneratePantunRequest
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface GeneratePantunService {

    @POST(ApiConstant.GENERATE_PANTUN)
    suspend fun generatePantun(@Body request: GeneratePantunRequest): GeneratePantunResponse
}