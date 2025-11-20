package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.KamusDoneResponse
import com.firman.rima.batombe.data.remote.models.KamusResponse
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface KamusService {

    @GET(ApiConstant.KAMUS_LIST)
    suspend fun getAllKamus(
        @Header("Authorization") token: String
    ): KamusResponse

    @PATCH(ApiConstant.KAMUS_DONE_PROGRESS)
    suspend fun markKamusAsDone(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") id: String
    ): KamusDoneResponse
}