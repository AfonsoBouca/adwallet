package com.example.project_ad_wallet.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.data.repository.CharityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AdminCharitiesViewModel @Inject constructor(
    private val charityRepository: CharityRepository
) : ViewModel() {
    val pendingCharities = charityRepository.charities
        .map { charities -> charities.filter { it.status == CharityStatus.PENDING } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun approveCharity(charityId: String) {
        charityRepository.approveCharity(charityId)
    }

    fun rejectCharity(charityId: String) {
        charityRepository.rejectCharity(charityId)
    }
}
