package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeDonationsDao : DonationsDao {
    private val _donations = MutableStateFlow<List<DonationEntity>>(emptyList())

    override fun getAllDonations(): Flow<List<DonationEntity>> = _donations

    override suspend fun insertDonation(donation: DonationEntity) {
        val currentList = _donations.value.toMutableList()
        currentList.add(0, donation)
        _donations.value = currentList
    }

    override fun getTotalDonated(): Flow<Double> {
        return _donations.map { it.sumOf { donation -> donation.amount } }
    }
}
