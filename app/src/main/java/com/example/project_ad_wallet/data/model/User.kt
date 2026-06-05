package com.example.project_ad_wallet.data.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val email: String = "",
    val password: String = "",
    val isBlocked: Boolean = false,
    val streakDays: Int = 0,
    val lastActiveDate: Long = 0L,
    val profilePictureUri: String? = null,
)
