package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val charityName: String,
    val charityId: String,
    val amount: Double,
    val sdgNumber: Int = 0,
    val sdgName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
