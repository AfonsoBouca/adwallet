package com.example.project_ad_wallet.ui.notifications

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationPrefsUiState(
    val newAdsEnabled: Boolean = true,
    val earningsEnabled: Boolean = true,
    val donationRemindersEnabled: Boolean = false,
    val weeklyDigestEnabled: Boolean = false,
)

class NotificationPrefsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationPrefsUiState())
    val uiState: StateFlow<NotificationPrefsUiState> = _uiState.asStateFlow()

    fun setNewAdsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(newAdsEnabled = enabled) }
    }

    fun setEarningsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(earningsEnabled = enabled) }
    }

    fun setDonationRemindersEnabled(enabled: Boolean) {
        _uiState.update { it.copy(donationRemindersEnabled = enabled) }
    }

    fun setWeeklyDigestEnabled(enabled: Boolean) {
        _uiState.update { it.copy(weeklyDigestEnabled = enabled) }
    }
}
