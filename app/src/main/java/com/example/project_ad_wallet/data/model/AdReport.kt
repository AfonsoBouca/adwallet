package com.example.project_ad_wallet.data.model

import java.util.UUID

data class AdReport(
    val id: String = UUID.randomUUID().toString(),
    val adId: String,
    val reason: String,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)
