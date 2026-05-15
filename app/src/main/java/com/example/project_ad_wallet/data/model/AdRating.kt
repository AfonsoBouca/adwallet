package com.example.project_ad_wallet.data.model

import java.util.UUID

data class AdRating(
    val id: String = UUID.randomUUID().toString(),
    val adId: String,
    val rating: Int,
    val timestamp: Long = System.currentTimeMillis(),
)
