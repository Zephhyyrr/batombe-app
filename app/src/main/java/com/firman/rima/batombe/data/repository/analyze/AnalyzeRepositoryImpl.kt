package com.firman.rima.batombe.data.repository.analyze

import com.firman.rima.batombe.data.local.datastore.AuthPreferences
import com.firman.rima.batombe.data.remote.models.AnalyzeResponse
import com.firman.rima.batombe.data.remote.request.AnalyzeRequest
import com.firman.rima.batombe.data.remote.service.AnalyzeService
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyzeRepositoryImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val analyzeService: AnalyzeService
) : AnalyzeRepository {

    override suspend fun analyzeText(text: String, audioFileName: String): Flow<ResultState<AnalyzeResponse>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""

        val request = AnalyzeRequest(
            text = text,
            audioFileName = audioFileName
        )

        val response = analyzeService.analyzeText("Bearer $token", request)

        emit(ResultState.Success(response))
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }
}