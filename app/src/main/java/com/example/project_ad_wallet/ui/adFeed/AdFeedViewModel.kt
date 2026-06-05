package com.example.project_ad_wallet.ui.adFeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.domain.StreakCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class AdFeedUiState(
    val ads: List<Ad> = emptyList(),
    val balance: Double = 0.0,
    val watchedAdIds: Set<String> = emptySet(),
    val streakDays: Int = 0,
    val showStreakBrokenNotification: Boolean = false,
)

class AdFeedViewModel(
    private val adRepository: AdRepository = AdRepository.instance,
    private val walletRepository: WalletRepository = WalletRepository.instance,
    private val userRepository: UserRepository = UserRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdFeedUiState())
    val uiState: StateFlow<AdFeedUiState> = _uiState.asStateFlow()

    init {
        combine(
            adRepository.ads,
            walletRepository.balance,
            adRepository.watchedAdIds,
            adRepository.skippedAdIds,
            userRepository.user,
        ) { ads, balance, watched, skipped, user ->
            val isBroken = StreakCalculator.isStreakBroken(user.lastActiveDate)
            AdFeedUiState(
                ads = ads.filter { it.status == AdStatus.APPROVED && it.id !in skipped },
                balance = balance,
                watchedAdIds = watched,
                streakDays = user.streakDays,
                showStreakBrokenNotification = isBroken && user.streakDays > 0
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun dismissStreakBrokenNotification() {
        _uiState.update { it.copy(showStreakBrokenNotification = false) }
    }
}