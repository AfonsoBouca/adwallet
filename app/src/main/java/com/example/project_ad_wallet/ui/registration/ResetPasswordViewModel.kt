package com.example.project_ad_wallet.ui.registration

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ResetPasswordUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isReset: Boolean = false,
)

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value, error = null) }
    }

    fun resetPassword() {
        val state = _uiState.value
        val email = state.email.trim()

        _uiState.update { it.copy(error = null) }

        when {
            email.isBlank() -> _uiState.update { it.copy(error = "Email is required") }
            state.password.isBlank() -> _uiState.update { it.copy(error = "New password is required") }
            state.password != state.confirmPassword -> _uiState.update { it.copy(error = "Passwords do not match") }
            !userRepository.hasUser(email) -> _uiState.update { it.copy(error = "No account found for that email") }
            else -> {
                userRepository.resetPassword(email, state.password)
                _uiState.update { it.copy(error = null, isReset = true) }
            }
        }
    }
}
