package com.example.project_ad_wallet.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EarningsHistoryViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {

    val earnings = walletRepository.getAllEarnings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalEarned = walletRepository.getTotalEarned()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val isLoading = MutableStateFlow(false)
        .asStateFlow()
}
