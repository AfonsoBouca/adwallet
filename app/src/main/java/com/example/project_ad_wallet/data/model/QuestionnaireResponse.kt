package com.example.project_ad_wallet.data.model

import java.util.UUID

data class QuestionnaireResponse(
    val id: String = UUID.randomUUID().toString(),
    val adId: String,
    val answers: List<Int> = emptyList(),
    val timestamp: Long = System.currentTimeMillis(),
)
