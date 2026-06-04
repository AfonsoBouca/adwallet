package com.example.project_ad_wallet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: Int = 0,
    val gender: String = "",
    val email: String,
    val walletBalance: Double = 0.0,
    val streakDays: Int = 0,
    val lastActiveDate: Long = 0L,
    val profilePictureUri: String? = null,
    val role: String = "USER"
)
