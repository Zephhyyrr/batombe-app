package com.firman.gita.batombe.data.repository.register

import com.firman.gita.batombe.data.remote.models.RegisterResponse
import com.firman.gita.batombe.utils.ResultState

interface RegisterRepository {
    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse>
}