package com.firman.rima.batombe.data.repository.kamus

import com.firman.rima.batombe.data.local.datastore.AuthPreferences
import com.firman.rima.batombe.data.remote.models.KamusDoneResponse
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.data.remote.service.KamusService
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KamusRepositoryImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val kamusService: KamusService
) : KamusRepository {
    override suspend fun getAllKamus(): Flow<ResultState<List<KamusResponse.Data>>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""
        val response = kamusService.getAllKamus("Bearer $token")

        if (response.success && response.data != null) {
            emit(ResultState.Success(response.data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to fetch articles"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }

    override suspend fun kamusDone(id: String): Flow<ResultState<KamusDoneResponse.Data>> = flow {
        emit(ResultState.Loading)

        val token = authPreferences.authToken.first() ?: ""
        val response = kamusService.markKamusAsDone("Bearer $token", id)

        if (response.success && response.data != null) {
            emit(ResultState.Success(response.data))
        } else {
            emit(ResultState.Error(response.message ?: "Failed to mark kamus as done"))
        }
    }.catch { e ->
        emit(ResultState.Error(e.message ?: "Unexpected error occurred"))
    }
}