package com.firman.rima.batombe.data.repository.example_video

import com.firman.rima.batombe.data.remote.models.ExampleVideoResponse
import com.firman.rima.batombe.data.remote.service.ExampleVideoService
import javax.inject.Inject

class ExampleVideoRepositoryImpl @Inject constructor(
    private val service: ExampleVideoService
) : ExampleVideoRepository {

    override suspend fun getAllVideos(): ExampleVideoResponse {
        return service.getAllVideos()
    }
}
