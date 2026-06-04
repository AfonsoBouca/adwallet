package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads_watched")
data class AdWatchedEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adId: String,
    val isReported: Boolean = false,
    val isSkipped: Boolean = false,
    val watchedAt: Long = System.currentTimeMillis()
)
