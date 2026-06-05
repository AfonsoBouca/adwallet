package com.example.project_ad_wallet.data.model

data class PlatformReport(
    val generatedAt: Long = System.currentTimeMillis(),
    val totalUsersActive: Int = 0,
    val totalAdsWatched: Int = 0,
    val totalDonations: Int = 0,
    val totalAmountDonated: Double = 0.0,
    val totalAmountEarned: Double = 0.0,
    val totalReports: Int = 0,
    val averageStreakDays: Double = 0.0,
    val topCharityName: String = "N/A",
    val topCharityDonations: Double = 0.0
)
