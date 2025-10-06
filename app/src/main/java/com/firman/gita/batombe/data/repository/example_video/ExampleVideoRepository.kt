package com.firman.gita.batombe.data.repository.example_video

import com.firman.gita.batombe.data.remote.models.ExampleVideoResponse

interface ExampleVideoRepository {
    suspend fun getAllVideos(): ExampleVideoResponse
}
