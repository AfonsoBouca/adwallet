package com.example.project_ad_wallet.ui.donations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.repository.DonationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DonationHistoryUiState(
    val donations: List<Donation> = emptyList(),
    val totalDonated: Double = 0.0,
    val isEmpty: Boolean = true,
)

@HiltViewModel
class DonationHistoryViewModel @Inject constructor(
    private val donationRepository: DonationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DonationHistoryUiState())
    val uiState: StateFlow<DonationHistoryUiState> = _uiState.asStateFlow()

    init {
        donationRepository.donations.onEach { donations ->
            val sorted = donations.sortedByDescending { it.timestamp }
            _uiState.update {
                it.copy(
                    donations = sorted,
                    totalDonated = donations.sumOf { d -> d.amount },
                    isEmpty = donations.isEmpty(),
                )
            }
        }.launchIn(viewModelScope)
    }
}
