package com.firman.gita.batombe.data.repository.user

import com.firman.gita.batombe.data.remote.models.CurrentUserResponse
import com.firman.gita.batombe.data.remote.models.DeleteUserResponse
import com.firman.gita.batombe.data.remote.models.UserLogoutResponse
import com.firman.gita.batombe.data.remote.models.UserProfileResponse
import com.firman.gita.batombe.data.remote.models.UserResponse
import com.firman.gita.batombe.utils.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UserRepository {
    suspend fun getCurrentUser(): Flow<ResultState<CurrentUserResponse>>
    suspend fun updateUser(name: String, password: String): Flow<ResultState<UserResponse>>
    suspend fun updateUserProfile(file: MultipartBody.Part): Flow<ResultState<UserProfileResponse>>
    suspend fun logout(): Flow<ResultState<UserLogoutResponse>>
    suspend fun deleteUser(): Flow<ResultState<DeleteUserResponse>>
}