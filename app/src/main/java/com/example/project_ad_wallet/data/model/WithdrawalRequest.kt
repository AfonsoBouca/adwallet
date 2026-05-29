package com.example.project_ad_wallet.data.model

enum class WithdrawalStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    REJECTED,
}

data class WithdrawalRequest(
    val id: String,
    val amount: Double,
    val bankAccount: String = "",
    val status: WithdrawalStatus = WithdrawalStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis(),
    val estimatedCompletionDate: Long? = null,
    val rejectionReason: String? = null,
)
