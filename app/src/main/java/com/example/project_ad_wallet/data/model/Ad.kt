package com.example.project_ad_wallet.data.model

enum class AdStatus { PENDING, APPROVED, REJECTED }

data class Ad(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailResId: Int,
    val videoResId: Int,
    val rewardAmount: Double,
    val advertiserName: String,
    val category: String,
    val durationSeconds: Int = 30,
    val isActive: Boolean = true,
)
