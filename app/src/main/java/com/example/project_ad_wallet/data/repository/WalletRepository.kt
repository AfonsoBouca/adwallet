package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.Earning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WalletRepository {

    companion object {
        val instance = WalletRepository()
    }

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _earnings = MutableStateFlow<List<Earning>>(emptyList())
    val earnings: StateFlow<List<Earning>> = _earnings.asStateFlow()

    fun addEarning(earning: Earning) {
        _earnings.update { (it + earning).sortedByDescending { e -> e.timestamp } }
        _balance.update { it + earning.amount }
    }

    fun addFunds(amount: Double) {
        _balance.update { it + amount }
    }

    fun generateRandomHistory(charities: List<com.example.project_ad_wallet.data.model.Charity>) {
        val randomEarnings = (1..5).map { i ->
            Earning(
                id = "rand_e_$i",
                adId = "ad_$i",
                adTitle = "Random Ad $i",
                amount = (0.10..0.50).random(),
                timestamp = System.currentTimeMillis() - (i * 3600000)
            )
        }
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
        
        _earnings.update { (it + randomEarnings).sortedByDescending { e -> e.timestamp } }
        val totalEarned = randomEarnings.sumOf { it.amount }
        val totalDonated = randomDonations.sumOf { it.amount }
        _balance.update { it + totalEarned - totalDonated }
        
        // We need to notify DonationRepository as well, but since we don't have a shared state, 
        // we'll just update WalletRepository's balance. 
        // In a real app, this would be handled by a use case or a shared database.
        // For now, I'll just add a way to inject donations if needed, but let's stick to balance/earnings.
    }

    private fun ClosedRange<Double>.random() = 
        java.util.concurrent.ThreadLocalRandom.current().nextDouble(start, endInclusive)

    fun deductBalance(amount: Double): Boolean {
        if (_balance.value < amount) return false
        _balance.update { it - amount }
        return true
    }

    fun reset() {
        _balance.value = 0.0
        _earnings.value = emptyList()
    }
}
