package com.example.project_ad_wallet.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AdminAdsViewModel @Inject constructor(
    private val adRepository: AdRepository
) : ViewModel() {
    val pendingAds = adRepository.ads
        .map { ads -> ads.filter { it.status == AdStatus.PENDING } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun approveAd(adId: String) {
        adRepository.approveAd(adId)
    }

    fun rejectAd(adId: String) {
        adRepository.rejectAd(adId)
    }
}
