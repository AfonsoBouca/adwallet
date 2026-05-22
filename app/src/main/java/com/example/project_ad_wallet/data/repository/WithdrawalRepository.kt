package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.model.WithdrawalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WithdrawalRepository {

    companion object {
        val instance = WithdrawalRepository()
    }

    private val _withdrawals = MutableStateFlow<List<WithdrawalRequest>>(emptyList())
    val withdrawals: StateFlow<List<WithdrawalRequest>> = _withdrawals.asStateFlow()

    fun addWithdrawal(withdrawal: WithdrawalRequest) {
        _withdrawals.update { (it + withdrawal).sortedByDescending { w -> w.timestamp } }
    }

    fun updateStatus(withdrawalId: String, newStatus: WithdrawalStatus) {
        _withdrawals.update { list ->
            list.map {
                if (it.id == withdrawalId) it.copy(status = newStatus) else it
            }
        }
    }

    fun generateSampleWithdrawals() {
        val now = System.currentTimeMillis()
        val samples = listOf(
            WithdrawalRequest(
                id = "w1",
                amount = 5.00,
                status = WithdrawalStatus.COMPLETED,
                timestamp = now - 5 * 86_400_000L,
                estimatedCompletionDate = now - 2 * 86_400_000L,
            ),
            WithdrawalRequest(
                id = "w2",
                amount = 3.50,
                status = WithdrawalStatus.PROCESSING,
                timestamp = now - 2 * 86_400_000L,
                estimatedCompletionDate = now + 1 * 86_400_000L,
            ),
            WithdrawalRequest(
                id = "w3",
                amount = 10.00,
                status = WithdrawalStatus.PENDING,
                timestamp = now - 3_600_000L,
                estimatedCompletionDate = now + 3 * 86_400_000L,
            ),
            WithdrawalRequest(
                id = "w4",
                amount = 2.00,
                status = WithdrawalStatus.REJECTED,
                timestamp = now - 7 * 86_400_000L,
                rejectionReason = "Insufficient verified account information",
            ),
        )
        _withdrawals.update { (it + samples).sortedByDescending { w -> w.timestamp } }
    }

    fun reset() {
        _withdrawals.value = emptyList()
    }
}
