package com.example.project_ad_wallet.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class WalletUiState(
    val balance: Double = 0.0,
    val earnings: List<Earning> = emptyList(),
    val donations: List<Donation> = emptyList(),
    val withdrawals: List<WithdrawalRequest> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalDonated: Double = 0.0,
)

class WalletViewModel(
    private val walletRepository: WalletRepository = WalletRepository.instance,
    private val donationRepository: DonationRepository = DonationRepository.instance,
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        combine(
            walletRepository.balance,
            walletRepository.earnings,
            donationRepository.donations,
            withdrawalRepository.withdrawals,
        ) { balance, earnings, donations, withdrawals ->
            WalletUiState(
                balance = balance,
                earnings = earnings.sortedByDescending { it.timestamp },
                donations = donations.sortedByDescending { it.timestamp },
                withdrawals = withdrawals.sortedByDescending { it.timestamp },
                totalEarned = earnings.sumOf { it.amount },
                totalDonated = donations.sumOf { it.amount },
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }
}
