package com.firman.rima.batombe.data.remote.service

import com.firman.rima.batombe.data.remote.request.RegisterRequest
import com.firman.rima.batombe.data.remote.models.RegisterResponse
import com.firman.rima.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST(ApiConstant.USER_REGISTER)
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}
