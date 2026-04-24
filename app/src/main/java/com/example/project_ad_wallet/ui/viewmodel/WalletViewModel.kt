package com.example.project_ad_wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.domain.model.Ad
import com.example.project_ad_wallet.domain.model.AppUiState
import com.example.project_ad_wallet.domain.model.Charity
import com.example.project_ad_wallet.domain.model.DonationBanner
import com.example.project_ad_wallet.domain.model.DonationBannerType
import com.example.project_ad_wallet.domain.model.ThemeMode
import com.example.project_ad_wallet.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WalletViewModel : ViewModel() {

    private val seedAds = listOf(
        Ad(
            id = "nike-running-shoes",
            title = "Nike Running Shoes",
            reward = 0.50,
            imagePlaceholder = "Nike Running Shoes Preview",
        ),
        Ad(
            id = "coca-cola-refresh",
            title = "Coca-Cola Refresh",
            reward = 0.30,
            imagePlaceholder = "Coca-Cola Refresh Preview",
        ),
        Ad(
            id = "samsung-galaxy-s24",
            title = "Samsung Galaxy S24",
            reward = 0.75,
            imagePlaceholder = "Samsung Galaxy S24 Preview",
        ),
    )

    private val seedCharities = listOf(
        Charity(
            id = "zero-hunger-foundation",
            name = "Zero Hunger Foundation",
            description = "Provides food to those in need.",
            sdg = "SDG 2",
        ),
        Charity(
            id = "green-earth",
            name = "Green Earth",
            description = "Focuses on climate action and reforestation.",
            sdg = "SDG 13",
        ),
        Charity(
            id = "clean-water-initiative",
            name = "Clean Water Initiative",
            description = "Ensures access to clean water and sanitation.",
            sdg = "SDG 6",
        ),
    )

    private val _uiState = MutableStateFlow(
        AppUiState(
            ads = seedAds,
            charities = seedCharities,
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun registerUser(fullName: String, age: String, gender: String) {
        _uiState.update {
            it.copy(
                userProfile = UserProfile(
                    fullName = fullName.trim(),
                    age = age.trim(),
                    gender = gender,
                )
            )
        }
    }

    fun updateUserProfile(fullName: String, age: String, gender: String) {
        _uiState.update {
            it.copy(
                userProfile = it.userProfile.copy(
                    fullName = fullName.trim(),
                    age = age.trim(),
                    gender = gender,
                )
            )
        }
    }

    fun getAd(adId: String): Ad? = _uiState.value.ads.firstOrNull { it.id == adId }

    fun claimSurveyReward(adId: String) {
        val ad = getAd(adId) ?: return
        _uiState.update {
            it.copy(
                balance = it.balance + ad.reward,
            )
        }
    }

    fun donateToCharity(charityId: String, amount: Double): Boolean {
        val charity = _uiState.value.charities.firstOrNull { it.id == charityId } ?: return false
        val currentBalance = _uiState.value.balance
        if (amount <= 0.0 || amount > currentBalance) {
            _uiState.update {
                it.copy(
                    donationBanner = DonationBanner(
                        type = DonationBannerType.Error,
                        message = "Insufficient funds or invalid amount."
                    )
                )
            }
            return false
        }

        _uiState.update {
            it.copy(
                balance = it.balance - amount,
                donationBanner = DonationBanner(
                    type = DonationBannerType.Success,
                    message = "Thank you for supporting ${charity.sdg}!"
                )
            )
        }
        return true
    }

    fun setThemeMode(themeMode: ThemeMode) {
        _uiState.update {
            it.copy(settings = it.settings.copy(themeMode = themeMode))
        }
    }

    fun addFunds(amount: Double = 50.0) {
        _uiState.update {
            it.copy(balance = it.balance + amount)
        }
    }

    fun resetAppState() {
        _uiState.value = AppUiState(
            ads = seedAds,
            charities = seedCharities,
        )
    }
}
