package com.example.project_ad_wallet.data.auth

data class AuthCredentials(
    val email: String,
    val password: String,
    val role: UserRole,
    val displayName: String
)
