package com.example.project_ad_wallet.ui.adWatch

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.AdReport
import com.example.project_ad_wallet.data.repository.AdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.UserActivityRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

val REPORT_REASONS = listOf(
    "Misleading content",
    "Inappropriate content",
    "Spam or repetitive",
    "Offensive language",
    "Other",
)

data class AdReportUiState(
    val selectedReason: String? = null,
    val comment: String = "",
    val isSubmitted: Boolean = false,
)

@HiltViewModel
class AdReportViewModel @Inject constructor(
    private val adRepository: AdRepository,
    private val userRepository: UserRepository,
    private val userActivityRepository: UserActivityRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdReportUiState())
    val uiState: StateFlow<AdReportUiState> = _uiState.asStateFlow()

    fun selectReason(reason: String) {
        _uiState.update { it.copy(selectedReason = reason) }
    }

    fun onCommentChanged(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun submitReport(adId: String) {
        val reason = _uiState.value.selectedReason ?: return
        adRepository.addReport(
            AdReport(
                adId = adId,
                reason = reason,
                comment = _uiState.value.comment.trim(),
            )
        )
        val user = userRepository.user.value
        viewModelScope.launch {
            userActivityRepository.logActivity(
                userName = user.name.ifBlank { "User" },
                activityType = "REPORT",
                details = "Reported ad $adId for $reason",
                userId = 1
            )
        }
        _uiState.update { it.copy(isSubmitted = true) }
    }

    fun reset() {
        _uiState.value = AdReportUiState()
    }
}
