package com.example.project_ad_wallet.ui.registration

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoggedIn: Boolean = false,
)

class LoginViewModel(
    private val userRepository: UserRepository = UserRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun login() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        when {
            email.isBlank() -> _uiState.update { it.copy(error = "Email is required") }
            password.isBlank() -> _uiState.update { it.copy(error = "Password is required") }
            !userRepository.authenticate(email, password) -> _uiState.update { it.copy(error = "Invalid email or password") }
            else -> {
                userRepository.getUser(email)?.let { userRepository.saveUser(it) }
                _uiState.update { it.copy(error = null, isLoggedIn = true) }
            }
        }
    }
}
