package com.example.project_ad_wallet.ui.notifications

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationPrefsUiState(
    val masterEnabled: Boolean = true,
    val newAdsEnabled: Boolean = true,
    val earningsEnabled: Boolean = true,
    val donationRemindersEnabled: Boolean = true,
    val weeklyDigestEnabled: Boolean = false,
    val promotionsEnabled: Boolean = false,
)

class NotificationPrefsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(loadPrefs())
    val uiState: StateFlow<NotificationPrefsUiState> = _uiState.asStateFlow()

    private fun loadPrefs(): NotificationPrefsUiState {
        return NotificationPrefsUiState(
            masterEnabled = prefs.getBoolean("master_enabled", true),
            newAdsEnabled = prefs.getBoolean("new_ads_enabled", true),
            earningsEnabled = prefs.getBoolean("earnings_enabled", true),
            donationRemindersEnabled = prefs.getBoolean("donation_enabled", true),
            weeklyDigestEnabled = prefs.getBoolean("weekly_enabled", false),
            promotionsEnabled = prefs.getBoolean("promotions_enabled", false),
        )
    }

    private fun savePref(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun setMasterEnabled(enabled: Boolean) {
        if (enabled) {
            // Restore from storage
            _uiState.value = loadPrefs().copy(masterEnabled = true)
        } else {
            // Disable all in UI, but keep storage as is
            _uiState.update { 
                it.copy(
                    masterEnabled = false,
                    newAdsEnabled = false,
                    earningsEnabled = false,
                    donationRemindersEnabled = false,
                    weeklyDigestEnabled = false,
                    promotionsEnabled = false,
                ) 
            }
        }
        savePref("master_enabled", enabled)
    }

    fun setNewAdsEnabled(enabled: Boolean) {
        if (!_uiState.value.masterEnabled) return
        _uiState.update { it.copy(newAdsEnabled = enabled) }
        savePref("new_ads_enabled", enabled)
    }

    fun setEarningsEnabled(enabled: Boolean) {
        if (!_uiState.value.masterEnabled) return
        _uiState.update { it.copy(earningsEnabled = enabled) }
        savePref("earnings_enabled", enabled)
    }

    fun setDonationRemindersEnabled(enabled: Boolean) {
        if (!_uiState.value.masterEnabled) return
        _uiState.update { it.copy(donationRemindersEnabled = enabled) }
        savePref("donation_enabled", enabled)
    }

    fun setWeeklyDigestEnabled(enabled: Boolean) {
        if (!_uiState.value.masterEnabled) return
        _uiState.update { it.copy(weeklyDigestEnabled = enabled) }
        savePref("weekly_enabled", enabled)
    }

    fun setPromotionsEnabled(enabled: Boolean) {
        if (!_uiState.value.masterEnabled) return
        _uiState.update { it.copy(promotionsEnabled = enabled) }
        savePref("promotions_enabled", enabled)
    }
}
