package com.example.project_ad_wallet.data.model

data class Donation(
    val id: String,
    val charityId: String,
    val charityName: String,
    val amount: Double,
    val sdgNumber: Int,
    val sdgName: String,
    val timestamp: Long = System.currentTimeMillis(),
)
