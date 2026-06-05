package com.example.project_ad_wallet.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.auth.AuthRepository
import com.example.project_ad_wallet.data.auth.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    private val _loginSuccess = MutableSharedFlow<UserRole>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    fun onEmailChange(value: String) { email = value }
    fun onPasswordChange(value: String) { password = value }

    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val user = authRepository.login(email, password)
            if (user != null) {
                _loginSuccess.emit(user.role)
            } else {
                errorMessage = "Email ou password incorretos."
            }
            isLoading = false
        }
    }
}
