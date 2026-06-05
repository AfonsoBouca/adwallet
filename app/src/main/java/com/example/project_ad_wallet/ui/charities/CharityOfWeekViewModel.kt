package com.example.project_ad_wallet.ui.charities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_ad_wallet.data.repository.CharityOfWeekRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharityOfWeekViewModel @Inject constructor(
    private val charityOfWeekRepository: CharityOfWeekRepository
) : ViewModel() {

    val charityOfWeek = charityOfWeekRepository.getCharityOfWeek()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            charityOfWeekRepository.initializeIfEmpty()
        }
    }

    fun donate(amount: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            charityOfWeekRepository.addDonation(amount)
            onSuccess()
        }
    }
}
