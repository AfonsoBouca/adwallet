package com.example.project_ad_wallet.ui.withdrawals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.model.WithdrawalStatus
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class WithdrawalStatusUiState(
    val withdrawals: List<WithdrawalRequest> = emptyList(),
    val totalWithdrawn: Double = 0.0,
    val pendingCount: Int = 0,
    val isEmpty: Boolean = true,
)

@HiltViewModel
class WithdrawalStatusViewModel @Inject constructor(
    private val withdrawalRepository: WithdrawalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalStatusUiState())
    val uiState: StateFlow<WithdrawalStatusUiState> = _uiState.asStateFlow()

    init {
        withdrawalRepository.withdrawals.onEach { withdrawals ->
            val sorted = withdrawals.sortedByDescending { it.timestamp }
            _uiState.update {
                it.copy(
                    withdrawals = sorted,
                    totalWithdrawn = withdrawals
                        .filter { w -> w.status == WithdrawalStatus.COMPLETED }
                        .sumOf { w -> w.amount },
                    pendingCount = withdrawals.count { w ->
                        w.status == WithdrawalStatus.PENDING || w.status == WithdrawalStatus.PROCESSING
                    },
                    isEmpty = withdrawals.isEmpty(),
                )
            }
        }.launchIn(viewModelScope)
    }
}
