package com.example.project_ad_wallet.ui.withdrawals

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

data class WithdrawalRequestUiState(
    val errorMessage: String? = null,
    val requestSent: Boolean = false,
)

@HiltViewModel
class WithdrawalRequestViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val withdrawalRepository: WithdrawalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalRequestUiState())
    val uiState: StateFlow<WithdrawalRequestUiState> = _uiState.asStateFlow()

    fun requestWithdrawal(amountText: String, bankAccount: String) {
        val amount = amountText.toDoubleOrNull()

        when {
            amount == null -> _uiState.update { it.copy(errorMessage = "Enter a valid amount") }
            amount <= 0.0 -> _uiState.update { it.copy(errorMessage = "Withdrawal amount must be greater than zero") }
            bankAccount.isBlank() -> _uiState.update { it.copy(errorMessage = "Bank account number is required") }
            walletRepository.balance.value < amount -> _uiState.update { it.copy(errorMessage = "Insufficient balance. Earn more before withdrawing.") }
            else -> {
                walletRepository.deductBalance(amount)
                val request = WithdrawalRequest(
                    id = UUID.randomUUID().toString(),
                    amount = amount,
                    bankAccount = bankAccount,
                    status = com.example.project_ad_wallet.data.model.WithdrawalStatus.PENDING,
                    estimatedCompletionDate = System.currentTimeMillis() + 3 * 86_400_000L,
                )
                withdrawalRepository.addWithdrawal(request)
                _uiState.update { it.copy(errorMessage = null, requestSent = true) }
            }
        }
    }
}
