package com.example.project_ad_wallet.data.model

data class Earning(
    val id: String,
    val adId: String,
    val adTitle: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
)
