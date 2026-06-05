package com.example.project_ad_wallet.data.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val credentials = listOf(
        AuthCredentials("user@adwallet.com", "user123", UserRole.USER, "Demo User"),
        AuthCredentials("admin@adwallet.com", "admin123", UserRole.ADMIN, "Admin"),
        AuthCredentials("advertiser@adwallet.com", "adv123", UserRole.ADVERTISER, "Demo Advertiser")
    )

    private var currentUser: AuthCredentials? = null

    fun login(email: String, password: String): AuthCredentials? {
        val user = credentials.find {
            it.email == email.trim() && it.password == password
        }
        currentUser = user
        return user
    }

    fun logout() { currentUser = null }

    fun getCurrentUser(): AuthCredentials? = currentUser

    fun isLoggedIn(): Boolean = currentUser != null
}
