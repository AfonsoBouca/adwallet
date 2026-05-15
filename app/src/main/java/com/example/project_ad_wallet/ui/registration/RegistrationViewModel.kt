package com.example.project_ad_wallet.ui.registration

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistrationUiState(
    val fullName: String = "",
    val age: String = "",
    val gender: String = "",
    val isRegistered: Boolean = false,
)

class RegistrationViewModel(
    private val userRepository: UserRepository = UserRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) {
        _uiState.update { it.copy(fullName = value) }
    }

    fun onAgeChanged(value: String) {
        _uiState.update { it.copy(age = value) }
    }

    fun onGenderChanged(value: String) {
        _uiState.update { it.copy(gender = value) }
    }

    fun register() {
        val state = _uiState.value
        userRepository.saveUser(
            User(
                name = state.fullName.trim(),
                age = state.age.trim(),
                gender = state.gender,
            )
        )
        _uiState.update { it.copy(isRegistered = true) }
    }

    val isFormValid: Boolean
        get() = _uiState.value.let {
            it.fullName.isNotBlank() && it.age.isNotBlank() && it.gender.isNotBlank()
        }
}
