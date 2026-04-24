package com.example.project_ad_wallet.domain.model

enum class DonationBannerType {
    Success,
    Error,
}

data class UserProfile(
    val fullName: String = "",
    val age: String = "",
    val gender: String = "",
)

data class Ad(
    val id: String,
    val title: String,
    val reward: Double,
    val imagePlaceholder: String,
)

data class Charity(
    val id: String,
    val name: String,
    val description: String,
    val sdg: String,
)

data class DonationBanner(
    val type: DonationBannerType,
    val message: String,
)

enum class ThemeMode {
    System,
    Light,
    Dark,
}

data class WalletSettings(
    val themeMode: ThemeMode = ThemeMode.System,
)

data class AppUiState(
    val balance: Double = 15.00,
    val userProfile: UserProfile = UserProfile(),
    val ads: List<Ad> = emptyList(),
    val charities: List<Charity> = emptyList(),
    val donationBanner: DonationBanner? = null,
    val settings: WalletSettings = WalletSettings(),
)
