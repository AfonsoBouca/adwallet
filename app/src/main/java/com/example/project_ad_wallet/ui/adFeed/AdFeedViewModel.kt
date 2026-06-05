package com.example.project_ad_wallet.ui.adFeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.domain.StreakCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AdFeedUiState(
    val ads: List<Ad> = emptyList(),
    val balance: Double = 0.0,
    val watchedAdIds: Set<String> = emptySet(),
    val streakDays: Int = 0,
    val showStreakBrokenNotification: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userRole: String = "USER"
)

@HiltViewModel
class AdFeedViewModel @Inject constructor(
    private val adRepository: AdRepository,
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdFeedUiState())
    val uiState: StateFlow<AdFeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        combine(
            adRepository.ads,
            walletRepository.balance,
            adRepository.watchedAdIds,
            adRepository.skippedAdIds,
            userRepository.user,
        ) { ads, balance, watched, skipped, user ->
            val isBroken = StreakCalculator.isStreakBroken(user.lastActiveDate)
            val filteredAds = adRepository.filterAdsForUser(ads, user)
            AdFeedUiState(
                ads = filteredAds.filter { it.id !in skipped },
                balance = balance,
                watchedAdIds = watched,
                streakDays = user.streakDays,
                showStreakBrokenNotification = isBroken && user.streakDays > 0,
                isLoading = false,
                userRole = user.role
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }

    fun dismissStreakBrokenNotification() {
        _uiState.update { it.copy(showStreakBrokenNotification = false) }
    }

    fun retry() {
        loadFeed()
    }
}
