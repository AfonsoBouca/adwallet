package com.example.project_ad_wallet.ui.donations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.repository.DonationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class DonationHistoryUiState(
    val donations: List<Donation> = emptyList(),
    val totalDonated: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DonationHistoryViewModel(
    private val donationRepository: DonationRepository = DonationRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DonationHistoryUiState())
    val uiState: StateFlow<DonationHistoryUiState> = _uiState.asStateFlow()

    init {
        loadDonations()
    }

    fun loadDonations() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        donationRepository.donations.onEach { donations ->
            val sorted = donations.sortedByDescending { it.timestamp }
            _uiState.update {
                it.copy(
                    donations = sorted,
                    totalDonated = donations.sumOf { d -> d.amount },
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun retry() {
        loadDonations()
    }
}
