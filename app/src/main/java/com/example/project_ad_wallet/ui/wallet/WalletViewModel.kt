package com.example.project_ad_wallet.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class WalletUiState(
    val balance: Double = 0.0,
    val earnings: List<Earning> = emptyList(),
    val donations: List<Donation> = emptyList(),
    val withdrawals: List<WithdrawalRequest> = emptyList(),
    val totalEarned: Double = 0.0,
    val totalDonated: Double = 0.0,
    val users: List<User> = emptyList(), // Nova propriedade para a UI do Admin
)

class WalletViewModel(
    private val walletRepository: WalletRepository = WalletRepository.instance,
    private val donationRepository: DonationRepository = DonationRepository.instance,
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository.instance,
    private val userRepository: UserRepository = UserRepository.instance // Novo repositório de utilizadores
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        // Agora o combine suporta até 5 fluxos
        combine(
            walletRepository.balance,
            walletRepository.earnings,
            donationRepository.donations,
            withdrawalRepository.withdrawals,
            userRepository.allUsers // Observamos a lista de utilizadores
        ) { balance, earnings, donations, withdrawals, users ->
            WalletUiState(
                balance = balance,
                earnings = earnings.sortedByDescending { it.timestamp },
                donations = donations.sortedByDescending { it.timestamp },
                withdrawals = withdrawals.sortedByDescending { it.timestamp },
                totalEarned = earnings.sumOf { it.amount },
                totalDonated = donations.sumOf { it.amount },
                users = users
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    // Função para o Admin bloquear/desbloquear
    fun toggleUserBlockStatus(userId: String) {
        userRepository.toggleBlockStatus(userId)
    }
}