package com.firman.rima.batombe.data.repository.speech

import com.firman.rima.batombe.data.remote.models.SpeechResponse
import com.firman.rima.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SpeechRepository {
    suspend fun speechToText(audioFile: File): Flow<ResultState<SpeechResponse.Data>>
}