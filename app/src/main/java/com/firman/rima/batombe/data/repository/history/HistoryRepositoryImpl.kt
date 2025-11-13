package com.firman.rima.batombe.data.repository.history

import com.firman.rima.batombe.data.local.datastore.AuthPreferences
import com.firman.rima.batombe.data.remote.models.HistoryResponse
import com.firman.rima.batombe.data.remote.service.HistoryService
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val historyService: HistoryService
) : HistoryRepository {

    override suspend fun saveHistory(
        audioFile: File,
        pantunBatombe: String
    ): Flow<ResultState<HistoryResponse.Data>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""

        val pantunRequestBody = pantunBatombe.toRequestBody("text/plain".toMediaTypeOrNull())
        val audioRequestBody = audioFile.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val audioFilePart = MultipartBody.Part.createFormData(
            "audioFile",
            audioFile.name,
            audioRequestBody
        )

        val response = historyService.saveHistory("Bearer $token", audioFilePart, pantunRequestBody)
        val data = response.data

        if (response.success && data != null) {
            emit(ResultState.Success(data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to save history"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }

    override suspend fun getAllHistory(): Flow<ResultState<List<HistoryResponse.Data>>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""
        val response = historyService.getAllHistory("Bearer $token")

        if (response.success && response.data != null) {
            emit(ResultState.Success(response.data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to fetch history"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }

    override suspend fun getHistoryById(id: Int): Flow<ResultState<HistoryResponse.Data>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""
        val response = historyService.getHistoryById("Bearer $token", id)
        val data = response.data

        if (response.success && data != null) {
            emit(ResultState.Success(data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to fetch history detail"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }
}