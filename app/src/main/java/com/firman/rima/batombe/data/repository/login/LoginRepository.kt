package com.firman.rima.batombe.data.repository.login

import com.firman.rima.batombe.data.remote.models.LoginResponse
import com.firman.rima.batombe.utils.ResultState

interface LoginRepository {
    suspend fun login(email: String, password: String): ResultState<LoginResponse>
    suspend fun refreshSession(email: String, password: String): ResultState<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun logout(): ResultState<Unit>
}