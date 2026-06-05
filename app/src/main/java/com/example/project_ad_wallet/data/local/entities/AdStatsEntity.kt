package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ad_stats")
data class AdStatsEntity(
    @PrimaryKey val adId: String,
    val adTitle: String,
    val totalViews: Int = 0,
    val totalCompletions: Int = 0,
    val totalSkips: Int = 0,
    val totalReports: Int = 0,
    val totalEarningsGenerated: Double = 0.0,
    val averageRating: Double = 0.0,
    val ratingCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
