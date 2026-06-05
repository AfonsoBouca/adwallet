package com.example.project_ad_wallet.ui.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.onboardingPages
import com.example.project_ad_wallet.data.preferences.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences
) : ViewModel() {

    var currentPage by mutableIntStateOf(0)
        private set

    val isLastPage: Boolean get() = currentPage == onboardingPages.size - 1

    fun nextPage() {
        if (!isLastPage) currentPage++
    }

    fun previousPage() {
        if (currentPage > 0) currentPage--
    }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            onboardingPreferences.setOnboardingCompleted()
            onComplete()
        }
    }
}
