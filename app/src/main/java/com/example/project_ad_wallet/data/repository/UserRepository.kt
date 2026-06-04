package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao? = null
) {

    companion object {
        // Para retrocompatibilidade enquanto não mudamos tudo
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

    private val _allUsers = MutableStateFlow<List<User>>(
        listOf(
            User(name = "João Silva", age = 22, gender = "M", isBlocked = false),
            User(name = "Ana Costa", age = 28, gender = "F", isBlocked = true),
            User(name = "Carlos Martins", age = 35, gender = "M", isBlocked = false)
        )
    )
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    fun toggleBlockStatus(userId: String) {
        _allUsers.update { currentList ->
            currentList.map { u ->
                if (u.id == userId) {
                    u.copy(isBlocked = !u.isBlocked)
                } else {
                    u
                }
            }
        }
    }
}
