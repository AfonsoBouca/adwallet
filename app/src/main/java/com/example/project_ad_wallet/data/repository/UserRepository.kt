package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepository {

    companion object {
        val instance = UserRepository()
    }

    // --- Lógica do Utilizador Ativo (Existente) ---
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.System)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun saveUser(user: User) {
        _user.value = user
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun reset() {
        _user.value = User()
        _themeMode.value = ThemeMode.System
    }

    // --- Lógica do Administrador: Gerir todos os utilizadores (Nova) ---
    private val _allUsers = MutableStateFlow<List<User>>(
        listOf(
            // Dados mockados para podermos testar a UI do Admin
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