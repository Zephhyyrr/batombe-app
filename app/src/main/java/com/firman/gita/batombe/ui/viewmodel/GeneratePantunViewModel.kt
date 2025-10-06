package com.firman.gita.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.gita.batombe.data.remote.models.GeneratePantunResponse
import com.firman.gita.batombe.data.repository.generatePantun.GeneratePantunRepository
import com.firman.gita.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class GeneratePantunViewModel @Inject constructor( private val generatePantunRepository: GeneratePantunRepository) : ViewModel() {
    private val _generatePantunState = MutableStateFlow<ResultState<GeneratePantunResponse>>(ResultState.Initial)
    val generatePantunState: StateFlow<ResultState<GeneratePantunResponse>> = _generatePantunState

    fun generatePantun(jumlah_baris: Int, tema: String, emosi: String){
        viewModelScope.launch {
            try {
                _generatePantunState.value = ResultState.Loading
                val result = generatePantunRepository.generatePantun(jumlah_baris, tema, emosi)
                _generatePantunState.value = result
            } catch (e: Exception) {
                _generatePantunState.value = ResultState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun getResultPantun(): StateFlow<ResultState<GeneratePantunResponse>> {
        return generatePantunState
    }

    fun resetState() {
        _generatePantunState.value = ResultState.Initial
    }
}