package com.firman.gita.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.gita.batombe.data.remote.models.RegisterResponse
import com.firman.gita.batombe.data.repository.register.RegisterRepository
import com.firman.gita.batombe.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<ResultState<RegisterResponse>>(ResultState.Initial)
    val registerState: StateFlow<ResultState<RegisterResponse>> = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _registerState.value = ResultState.Loading
                val result = registerRepository.register(name, email, password)
                _registerState.value = result
            } catch (e: Exception) {
                _registerState.value = ResultState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
