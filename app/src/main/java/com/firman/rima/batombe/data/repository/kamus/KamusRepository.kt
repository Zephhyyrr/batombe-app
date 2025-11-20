package com.firman.rima.batombe.data.repository.kamus

import com.firman.rima.batombe.data.remote.models.KamusDoneResponse
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface KamusRepository {
    suspend fun getAllKamus(): Flow<ResultState<List<KamusResponse.Data>>>
    suspend fun kamusDone(id: String): Flow<ResultState<KamusDoneResponse.Data>>
}