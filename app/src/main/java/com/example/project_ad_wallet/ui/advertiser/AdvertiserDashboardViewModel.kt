package com.example.project_ad_wallet.ui.advertiser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import com.example.project_ad_wallet.data.repository.AdStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AdvertiserDashboardViewModel @Inject constructor(
    private val adStatsRepository: AdStatsRepository
) : ViewModel() {

    val adStats: StateFlow<List<AdStatsEntity>> = adStatsRepository.getAllAdStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalViews: StateFlow<Int> = adStatsRepository.getTotalViews()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCompletions: StateFlow<Int> = adStatsRepository.getTotalCompletions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completionRate: StateFlow<Double> = combine(
        totalViews, totalCompletions
    ) { views, completions ->
        if (views > 0) (completions.toDouble() / views) * 100 else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}
