package com.firman.rima.batombe.data.repository.history

import com.firman.rima.batombe.data.remote.models.HistoryResponse
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface HistoryRepository {
    suspend fun saveHistory(
        audioFile: File,
        pantunBatombe: String
    ): Flow<ResultState<HistoryResponse.Data>>

    suspend fun getAllHistory(): Flow<ResultState<List<HistoryResponse.Data>>>

    suspend fun getHistoryById(id: Int): Flow<ResultState<HistoryResponse.Data>>
}