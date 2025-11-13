package com.firman.rima.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.rima.batombe.data.remote.models.ExampleVideoResponse
import com.firman.rima.batombe.data.repository.example_video.ExampleVideoRepository
import com.firman.rima.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleVideoViewModel @Inject constructor(
    private val repository: ExampleVideoRepository
) : ViewModel() {

    private val _exampleVideos =
        MutableStateFlow<ResultState<ExampleVideoResponse>>(ResultState.Loading)
    val exampleVideos: StateFlow<ResultState<ExampleVideoResponse>> = _exampleVideos

    fun getAllVideos() {
        viewModelScope.launch {
            try {
                _exampleVideos.value = ResultState.Loading
                val response = repository.getAllVideos()

                if (response.success) {
                    _exampleVideos.value = ResultState.Success(response)
                } else {
                    _exampleVideos.value =
                        ResultState.Error(response.message ?: "Failed to load videos")
                }
            } catch (e: Exception) {
                _exampleVideos.value = ResultState.Error(e.localizedMessage ?: "Unexpected error")
            }
        }
    }
}
