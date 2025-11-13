package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.SpeechResponse
import com.firman.rima.batombe.utils.ApiConstant
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SpeechService {
    @Multipart
    @POST(ApiConstant.SPEECH_TO_TEXT)
    suspend fun speechToText(
        @Header("Authorization") token: String,
        @Part audio: MultipartBody.Part
    ): SpeechResponse
}