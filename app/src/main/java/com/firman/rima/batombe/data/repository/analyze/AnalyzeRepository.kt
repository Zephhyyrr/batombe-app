package com.firman.rima.batombe.data.repository.analyze

import com.firman.rima.batombe.data.remote.models.AnalyzeResponse
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AnalyzeRepository {
    suspend fun analyzeText(text: String, audioFileName: String): Flow<ResultState<AnalyzeResponse>>
}
