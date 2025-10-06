package com.firman.gita.batombe.data.remote.service

import com.firman.gita.batombe.data.remote.request.RegisterRequest
import com.firman.gita.batombe.data.remote.models.RegisterResponse
import com.firman.gita.batombe.utils.ApiConstant
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST(ApiConstant.USER_REGISTER)
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}
