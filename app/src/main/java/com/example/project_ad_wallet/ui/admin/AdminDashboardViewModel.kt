package com.example.project_ad_wallet.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import com.example.project_ad_wallet.data.repository.UserActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val userActivityRepository: UserActivityRepository
) : ViewModel() {

    val recentActivity: StateFlow<List<UserActivityEntity>> = userActivityRepository.getRecentActivity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flaggedActivity: StateFlow<List<UserActivityEntity>> = userActivityRepository.getFlaggedActivity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flaggedCount: StateFlow<Int> = flaggedActivity
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun flagActivity(id: Long, reason: String) {
        viewModelScope.launch {
            userActivityRepository.flagActivity(id, reason)
        }
    }
}
