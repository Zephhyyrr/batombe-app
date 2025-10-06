package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.request.LoginRequest
import com.firman.gita.batombe.data.remote.models.LoginResponse
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(ApiConstant.USER_LOGIN)
    suspend fun login(@Body request: LoginRequest): LoginResponse
}