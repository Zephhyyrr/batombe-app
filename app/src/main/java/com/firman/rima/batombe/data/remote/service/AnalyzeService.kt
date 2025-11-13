package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.AnalyzeResponse
import com.firman.rima.batombe.data.remote.request.AnalyzeRequest
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnalyzeService {
    @POST(ApiConstant.ANALYZE_TEXT)
    suspend fun analyzeText(
        @Header("Authorization") authorization: String,
        @Body request: AnalyzeRequest
    ): AnalyzeResponse
}