package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_activity")
data class UserActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int = 1,
    val userName: String,
    val activityType: String, // "AD_WATCHED", "DONATION", "REPORT", "LOGIN"
    val details: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFlagged: Boolean = false,
    val flagReason: String? = null
)
