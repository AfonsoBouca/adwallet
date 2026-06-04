package com.example.project_ad_wallet.auth

import com.example.project_ad_wallet.data.auth.AuthRepository
import com.example.project_ad_wallet.data.auth.UserRole
import org.junit.Assert.*
import org.junit.Test

class AuthRepositoryTest {

    private val authRepository = AuthRepository()

    @Test
    fun login_withValidUserCredentials_returnsUserRole() {
        val result = authRepository.login("user@adwallet.com", "user123")
        assertNotNull(result)
        assertEquals(UserRole.USER, result?.role)
    }

    @Test
    fun login_withValidAdminCredentials_returnsAdminRole() {
        val result = authRepository.login("admin@adwallet.com", "admin123")
        assertNotNull(result)
        assertEquals(UserRole.ADMIN, result?.role)
    }

    @Test
    fun login_withValidAdvertiserCredentials_returnsAdvertiserRole() {
        val result = authRepository.login("advertiser@adwallet.com", "adv123")
        assertNotNull(result)
        assertEquals(UserRole.ADVERTISER, result?.role)
    }

    @Test
    fun login_withInvalidCredentials_returnsNull() {
        val result = authRepository.login("wrong@email.com", "wrongpass")
        assertNull(result)
    }

    @Test
    fun logout_clearsCurrentUser() {
        authRepository.login("user@adwallet.com", "user123")
        authRepository.logout()
        assertNull(authRepository.getCurrentUser())
    }

    @Test
    fun isLoggedIn_afterLogin_returnsTrue() {
        authRepository.login("user@adwallet.com", "user123")
        assertTrue(authRepository.isLoggedIn())
    }

    @Test
    fun isLoggedIn_afterLogout_returnsFalse() {
        authRepository.login("user@adwallet.com", "user123")
        authRepository.logout()
        assertFalse(authRepository.isLoggedIn())
    }
}
