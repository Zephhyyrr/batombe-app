package com.firman.gita.batombe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.gita.batombe.data.remote.models.CurrentUserResponse
import com.firman.gita.batombe.data.remote.models.ExampleVideoResponse
import com.firman.gita.batombe.data.repository.example_video.ExampleVideoRepository
import com.firman.gita.batombe.data.repository.user.UserRepository
import com.firman.gita.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val exampleVideoRepository: ExampleVideoRepository
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _userState = MutableStateFlow<ResultState<CurrentUserResponse>>(ResultState.Initial)
    val userState: StateFlow<ResultState<CurrentUserResponse>> = _userState.asStateFlow()

    private val _exampleVideosState = MutableStateFlow<ResultState<ExampleVideoResponse>>(ResultState.Initial)
    val exampleVideosState: StateFlow<ResultState<ExampleVideoResponse>> = _exampleVideosState.asStateFlow()

    fun getCurrentUser() {
        viewModelScope.launch {
            Log.d(TAG, "getCurrentUser() called - Starting...")
            _userState.value = ResultState.Loading
            Log.d(TAG, "State set to Loading")

            try {
                Log.d(TAG, "Calling userRepository.getCurrentUser()")
                userRepository.getCurrentUser().collect { result ->
                    Log.d(TAG, "User result received: $result")
                    _userState.value = result
                    Log.d(TAG, "State updated with result: ${_userState.value}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in getCurrentUser: ${e.message}", e)
                _userState.value = ResultState.Error("Failed to load user: ${e.message}")
            }
        }
    }

    fun getAllExampleVideos() {
        viewModelScope.launch {
            Log.d(TAG, "getAllExampleVideos() called - Starting...")
            _exampleVideosState.value = ResultState.Loading

            try {
                Log.d(TAG, "Calling exampleVideoRepository.getAllVideos()")
                val response = exampleVideoRepository.getAllVideos()
                Log.d(TAG, "Example videos response received - success: ${response.success}, message: ${response.message}")

                if (response.success) {
                    _exampleVideosState.value = ResultState.Success(response)
                    Log.d(TAG, "Videos loaded successfully: ${response.data?.size ?: 0} items")
                } else {
                    _exampleVideosState.value = ResultState.Error(response.message ?: "Failed to load example videos")
                    Log.e(TAG, "Failed to load videos: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in getAllExampleVideos: ${e.message}", e)
                _exampleVideosState.value = ResultState.Error("Error loading example videos: ${e.message}")
            }
        }
    }

    fun loadInitialData() {
        Log.d(TAG, "loadInitialData() called")
        getCurrentUser()
        getAllExampleVideos()
    }
}