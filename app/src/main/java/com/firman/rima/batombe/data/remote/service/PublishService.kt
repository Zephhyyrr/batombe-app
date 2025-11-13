package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.PublishHistoryResponse
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface PublishService {

    @PUT(ApiConstant.PUBLISH_FEED)
    suspend fun publishFeed(
        @Header("Authorization") token: String,
        @Path("id") historyId: Int
    ): PublishHistoryResponse
}