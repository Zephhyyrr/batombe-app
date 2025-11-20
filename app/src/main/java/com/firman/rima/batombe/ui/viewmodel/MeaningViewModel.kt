package com.firman.rima.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.rima.batombe.data.remote.models.MeaningResponse
import com.firman.rima.batombe.data.repository.meaning.MeaningRepository
import com.firman.rima.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeaningViewModel @Inject constructor(
    private val meaningRepository: MeaningRepository
) : ViewModel() {

    private val _meaningState = MutableStateFlow<ResultState<MeaningResponse>>(ResultState.Initial)
    val meaningState: StateFlow<ResultState<MeaningResponse>> = _meaningState

    fun getMeaning(batombe: String) {
        viewModelScope.launch {
            try {
                _meaningState.value = ResultState.Loading
                val result = meaningRepository.generatePantun(batombe)
                _meaningState.value = result
            } catch (e: Exception) {
                _meaningState.value = ResultState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun getResultMeaning(): StateFlow<ResultState<MeaningResponse>> {
        return meaningState
    }

    fun resetState() {
        _meaningState.value = ResultState.Initial
    }
}