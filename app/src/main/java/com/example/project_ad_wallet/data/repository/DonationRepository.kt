package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.Donation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonationRepository {

    companion object {
        val instance = DonationRepository()
    }

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations: StateFlow<List<Donation>> = _donations.asStateFlow()

    fun addDonation(donation: Donation) {
        _donations.update { it + donation }
    }

    fun reset() {
        _donations.value = emptyList()
    }
}
