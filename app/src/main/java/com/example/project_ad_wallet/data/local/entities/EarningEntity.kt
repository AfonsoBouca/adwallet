package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earnings")
data class EarningEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adTitle: String,
    val adId: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis()
)
