package com.firman.gita.batombe.data.repository.example_video

import com.firman.gita.batombe.data.remote.models.ExampleVideoResponse
import com.firman.gita.batombe.data.remote.service.ExampleVideoService
import javax.inject.Inject

class ExampleVideoRepositoryImpl @Inject constructor(
    private val service: ExampleVideoService
) : ExampleVideoRepository {

    override suspend fun getAllVideos(): ExampleVideoResponse {
        return service.getAllVideos()
    }
}
