package com.example.project_ad_wallet.ui.advertiser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.repository.AdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AdvertiserResponsesViewModel @Inject constructor(
    private val adRepository: AdRepository
) : ViewModel() {
    val responses = adRepository.questionnaireResponses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getAdById(adId: String): Ad? = adRepository.getAdById(adId)
}
