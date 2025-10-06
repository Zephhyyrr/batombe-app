package com.firman.gita.batombe.data.repository.history

import com.firman.gita.batombe.data.remote.models.HistoryResponse
import com.firman.gita.batombe.data.remote.request.HistoryRequest
import com.firman.gita.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun saveHistory(
        audioFileName: String,
        originalParagraph: String,
        correctedParagraph: String,
        grammarAnalysis: List<HistoryRequest.GrammarAnalysis>
    ): Flow<ResultState<HistoryResponse.Data>>

    suspend fun getAllHistory(): Flow<ResultState<List<HistoryResponse.Data>>>

    suspend fun getHistoryById(id: Int): Flow<ResultState<HistoryResponse.Data>>
}
