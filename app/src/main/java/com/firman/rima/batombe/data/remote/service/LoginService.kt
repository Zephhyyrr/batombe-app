package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.request.LoginRequest
import com.firman.rima.batombe.data.remote.models.LoginResponse
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(ApiConstant.USER_LOGIN)
    suspend fun login(@Body request: LoginRequest): LoginResponse
}