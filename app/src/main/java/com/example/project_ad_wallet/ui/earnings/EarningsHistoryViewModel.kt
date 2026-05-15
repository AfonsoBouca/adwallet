package com.example.project_ad_wallet.ui.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class EarningsHistoryUiState(
    val earnings: List<Earning> = emptyList(),
    val totalEarned: Double = 0.0,
    val isEmpty: Boolean = true,
)

class EarningsHistoryViewModel(
    private val walletRepository: WalletRepository = WalletRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EarningsHistoryUiState())
    val uiState: StateFlow<EarningsHistoryUiState> = _uiState.asStateFlow()

    init {
        walletRepository.earnings.onEach { earnings ->
            val sorted = earnings.sortedByDescending { it.timestamp }
            _uiState.update {
                it.copy(
                    earnings = sorted,
                    totalEarned = earnings.sumOf { e -> e.amount },
                    isEmpty = earnings.isEmpty(),
                )
            }
        }.launchIn(viewModelScope)
    }
}
