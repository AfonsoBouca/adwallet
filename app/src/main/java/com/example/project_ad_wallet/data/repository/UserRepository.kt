package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository {

    companion object {
        val instance = UserRepository()
    }

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.System)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val registeredUsers = mutableMapOf<String, User>()

    fun saveUser(user: User) {
        _user.value = user
    }

    fun registerUser(user: User) {
        _user.value = user
        val normalizedEmail = user.email.trim().lowercase()
        if (normalizedEmail.isNotBlank()) {
            registeredUsers[normalizedEmail] = user
        }
    }

    fun authenticate(email: String, password: String): Boolean {
        val normalizedEmail = email.trim().lowercase()
        return registeredUsers[normalizedEmail]?.password == password
    }

    fun resetPassword(email: String, newPassword: String): Boolean {
        val normalizedEmail = email.trim().lowercase()
        val existing = registeredUsers[normalizedEmail] ?: return false
        val updated = existing.copy(password = newPassword)
        registeredUsers[normalizedEmail] = updated
        if (_user.value.email.trim().lowercase() == normalizedEmail) {
            _user.value = updated
        }
        return true
    }

    fun hasUser(email: String): Boolean {
        return registeredUsers.containsKey(email.trim().lowercase())
    }

    fun getUser(email: String): User? {
        return registeredUsers[email.trim().lowercase()]
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun logout() {
        _user.value = User()
    }

    fun reset() {
        _user.value = User()
        registeredUsers.clear()
        _themeMode.value = ThemeMode.System
    }
}
