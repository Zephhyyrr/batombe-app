package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.models.GeneratePantunResponse
import com.firman.gita.batombe.data.remote.request.GeneratePantunRequest
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface GeneratePantunService {

    @POST(ApiConstant.GENERATE_PANTUN)
    suspend fun generatePantun(@Body request: GeneratePantunRequest): GeneratePantunResponse
}