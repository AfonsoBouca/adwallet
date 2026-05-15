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
}
