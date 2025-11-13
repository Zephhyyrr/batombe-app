package com.firman.rima.batombe.data.repository.example_video

import com.firman.rima.batombe.data.remote.models.ExampleVideoResponse

interface ExampleVideoRepository {
    suspend fun getAllVideos(): ExampleVideoResponse
}
