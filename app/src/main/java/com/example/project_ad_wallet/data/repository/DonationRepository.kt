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

    fun generateRandomHistory(charities: List<com.example.project_ad_wallet.data.model.Charity>) {
        val randomDonations = (1..3).map { i ->
            val charity = charities.random()
            com.example.project_ad_wallet.data.model.Donation(
                id = "rand_d_$i",
                charityId = charity.id,
                charityName = charity.name,
                amount = (1.0..5.0).random(),
                sdgNumber = charity.sdgNumber,
                sdgName = charity.sdgName,
                timestamp = System.currentTimeMillis() - (i * 7200000)
            )
        }
        _donations.update { (it + randomDonations).sortedByDescending { d -> d.timestamp } }
    }

    private fun ClosedRange<Double>.random() = 
        java.util.concurrent.ThreadLocalRandom.current().nextDouble(start, endInclusive)

    fun reset() {
        _donations.value = emptyList()
    }
}
