package com.firman.rima.batombe.data.repository.register

import com.firman.rima.batombe.data.remote.models.RegisterResponse
import com.firman.rima.batombe.utils.ResultState

interface RegisterRepository {
    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse>
}