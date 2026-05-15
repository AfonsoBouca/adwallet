package com.example.project_ad_wallet.data.model

data class AdRating(
    val adId: String,
    val rating: Int,
    val timestamp: Long = System.currentTimeMillis(),
)
