package com.example.project_ad_wallet.ui.adWatch

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.AdReport
import com.example.project_ad_wallet.data.repository.AdRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

class AdReportViewModel(
    private val adRepository: AdRepository = AdRepository.instance,
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
        _uiState.update { it.copy(isSubmitted = true) }
    }

    fun reset() {
        _uiState.value = AdReportUiState()
    }
}
