package com.firman.rima.batombe.data.repository.register

import com.firman.rima.batombe.data.remote.models.RegisterResponse
import com.firman.rima.batombe.data.remote.request.RegisterRequest
import com.firman.rima.batombe.data.remote.service.RegisterService
import com.firman.rima.batombe.utils.ResultState
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerService: RegisterService
) : RegisterRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): ResultState<RegisterResponse> {
        return try {
            val request = RegisterRequest(name, email, password)
            val response = registerService.register(request)
            if (response.success) {
                ResultState.Success(response)
            } else {
                ResultState.Error(response.message ?: "Register failed")
            }
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error occurred")
        }
    }
}
