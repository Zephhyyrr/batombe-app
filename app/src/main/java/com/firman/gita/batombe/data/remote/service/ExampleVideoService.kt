package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.models.ExampleVideoResponse
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.GET

interface ExampleVideoService {

    @GET(ApiConstant.GET_ALL_EXAMPLE_VIDEOS)
    suspend fun getAllVideos(): ExampleVideoResponse
}
