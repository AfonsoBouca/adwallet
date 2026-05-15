package com.example.project_ad_wallet.data.model

data class AdReport(
    val adId: String,
    val reason: String,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)
