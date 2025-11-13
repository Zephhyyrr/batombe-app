package com.firman.rima.batombe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.rima.batombe.data.local.datastore.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences
) : ViewModel() {

    fun completeOnBoarding() {
        viewModelScope.launch {
            onboardingPreferences.setOnboardingStatus(true)
        }
    }

    suspend fun setOnBoardingComplete() {
        onboardingPreferences.setOnboardingStatus(true)
    }
}