package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.models.ExampleVideoResponse
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.GET

interface ExampleVideoService {

    @GET(ApiConstant.GET_ALL_EXAMPLE_VIDEOS)
    suspend fun getAllVideos(): ExampleVideoResponse
}
