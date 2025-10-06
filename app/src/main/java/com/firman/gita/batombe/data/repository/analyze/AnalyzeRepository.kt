package com.firman.gita.batombe.data.repository.analyze

import com.firman.gita.batombe.data.remote.models.AnalyzeResponse
import com.firman.gita.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AnalyzeRepository {
    suspend fun analyzeText(text: String, audioFileName: String): Flow<ResultState<AnalyzeResponse>>
}
