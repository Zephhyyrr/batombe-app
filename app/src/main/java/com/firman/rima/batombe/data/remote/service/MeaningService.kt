package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.MeaningResponse
import com.firman.rima.batombe.data.remote.request.MeaningRequest
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface MeaningService {

    @POST(ApiConstant.GENERATE_MEANING)
    suspend fun generateMeaning(@Body request: MeaningRequest): MeaningResponse
}