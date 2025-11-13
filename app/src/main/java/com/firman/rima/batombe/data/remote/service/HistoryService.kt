package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.HistoryDetailResponse
import com.firman.rima.batombe.data.remote.models.HistoryResponse
import com.firman.rima.batombe.data.remote.models.SaveHistoryResponse
import com.firman.rima.batombe.utils.ApiConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface HistoryService {

    @Multipart
    @POST(ApiConstant.SAVE_HISTORY)
    suspend fun saveHistory(
        @Header("Authorization") authorization: String,
        @Part audioFile: MultipartBody.Part,
        @Part("pantunBatombe") pantunBatombe: RequestBody
    ): SaveHistoryResponse

    @GET(ApiConstant.HISTORY_LIST)
    suspend fun getAllHistory(
        @Header("Authorization") authorization: String
    ): HistoryResponse

    @GET(ApiConstant.HISTORY_DETAIL)
    suspend fun getHistoryById(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): HistoryDetailResponse
}