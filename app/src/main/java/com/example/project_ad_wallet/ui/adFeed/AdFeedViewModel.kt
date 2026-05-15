package com.example.project_ad_wallet.ui.adFeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
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
)

class AdFeedViewModel(
    private val adRepository: AdRepository = AdRepository.instance,
    private val walletRepository: WalletRepository = WalletRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdFeedUiState())
    val uiState: StateFlow<AdFeedUiState> = _uiState.asStateFlow()

    init {
        combine(
            adRepository.ads,
            walletRepository.balance,
            adRepository.watchedAdIds,
        ) { ads, balance, watched ->
            AdFeedUiState(
                ads = ads.filter { it.isActive },
                balance = balance,
                watchedAdIds = watched,
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }
}
