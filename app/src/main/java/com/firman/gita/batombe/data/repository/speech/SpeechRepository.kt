package com.firman.gita.batombe.data.repository.speech

import com.firman.gita.batombe.data.remote.models.SpeechResponse
import com.firman.gita.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SpeechRepository {
    suspend fun speechToText(audioFile: File): Flow<ResultState<SpeechResponse.Data>>
}