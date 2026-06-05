package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charity_of_week")
data class CharityOfWeekEntity(
    @PrimaryKey val id: Int = 1,
    val charityId: String,
    val charityName: String,
    val charityDescription: String,
    val sdgNumber: Int,
    val sdgDescription: String,
    val logoRes: String,
    val weekStartTimestamp: Long = System.currentTimeMillis(),
    val totalDonatedThisWeek: Double = 0.0
)
