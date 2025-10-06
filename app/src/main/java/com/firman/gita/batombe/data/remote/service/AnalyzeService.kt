package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.models.AnalyzeResponse
import com.firman.gita.batombe.data.remote.request.AnalyzeRequest
import com.firman.gita.batombe.utils.ApiConstant
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