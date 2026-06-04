package com.example.project_ad_wallet.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.CharityRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val user: User = User(),
    val themeMode: ThemeMode = ThemeMode.System,
    val isLoading: Boolean = false,
    val error: String? = null
)

class SettingsViewModel(
    private val userRepository: UserRepository = UserRepository.instance,
    private val walletRepository: WalletRepository = WalletRepository.instance,
    private val adRepository: AdRepository = AdRepository.instance,
    private val donationRepository: DonationRepository = DonationRepository.instance,
    private val charityRepository: CharityRepository = CharityRepository.instance,
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        combine(
            userRepository.user,
            userRepository.themeMode,
        ) { user, themeMode ->
            _uiState.update { 
                it.copy(
                    user = user, 
                    themeMode = themeMode,
                    isLoading = false
                ) 
            }
        }.launchIn(viewModelScope)
    }

    fun retry() {
        loadSettings()
    }

    fun updateProfile(name: String, age: Int, gender: String) {
        userRepository.saveUser(User(name = name.trim(), age = age, gender = gender))
    }

    fun setThemeMode(mode: ThemeMode) {
        userRepository.setThemeMode(mode)
    }

    fun resetApp() {
        userRepository.reset()
        walletRepository.reset()
        adRepository.reset()
        donationRepository.reset()
        withdrawalRepository.reset()
    }

    fun onAddFunds(amount: Double) {
        walletRepository.addFunds(amount)
    }

    fun onGenerateRandomHistory() {
        val charitiesList = charityRepository.getCharities()
        walletRepository.generateRandomHistory(charitiesList)
        donationRepository.generateRandomHistory(charitiesList)
        withdrawalRepository.generateSampleWithdrawals()
    }

    fun logout() {
        userRepository.logout()
    }

    fun deleteAccount() {
        userRepository.reset()
        walletRepository.reset()
        adRepository.reset()
        donationRepository.reset()
        withdrawalRepository.reset()
    }
}
