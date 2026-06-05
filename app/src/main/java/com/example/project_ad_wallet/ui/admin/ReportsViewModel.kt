package com.example.project_ad_wallet.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.model.PlatformReport
import com.example.project_ad_wallet.data.repository.ReportsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsRepository: ReportsRepository
) : ViewModel() {

    var report by mutableStateOf<PlatformReport?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun generateReport() {
        viewModelScope.launch {
            isLoading = true
            report = reportsRepository.generateReport()
            isLoading = false
        }
    }
}
