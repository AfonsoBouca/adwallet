package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.model.Donation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DonationRepository(
    private val donationsDao: DonationsDao? = null,
    private val userProfileDao: UserProfileDao? = null
) {

    companion object {
        private var _instance: DonationRepository? = null
        val instance: DonationRepository
            get() = _instance ?: DonationRepository()

        fun initialize(donationsDao: DonationsDao, userProfileDao: UserProfileDao) {
            _instance = DonationRepository(donationsDao, userProfileDao)
        }
    }

    private val _donations = MutableStateFlow<List<Donation>>(emptyList())
    val donations: StateFlow<List<Donation>> = _donations.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        donationsDao?.getAllDonations()?.onEach { entities ->
            _donations.value = entities.map { entity ->
                Donation(
                    id = entity.id.toString(),
                    charityId = entity.charityId,
                    charityName = entity.charityName,
                    amount = entity.amount,
                    timestamp = entity.timestamp,
                    sdgNumber = entity.sdgNumber,
                    sdgName = entity.sdgName
                )
            }
        }?.launchIn(repositoryScope)
    }

    fun addDonation(donation: Donation) {
        repositoryScope.launch {
            donationsDao?.insertDonation(
                DonationEntity(
                    charityId = donation.charityId,
                    charityName = donation.charityName,
                    amount = donation.amount,
                    sdgNumber = donation.sdgNumber,
                    sdgName = donation.sdgName,
                    timestamp = donation.timestamp
                )
            )
            userProfileDao?.deductFromWalletBalance(donation.amount)
        }
        // Update the in‑memory flow for tests without a DAO
        _donations.update { it + donation }
    }

    fun generateRandomHistory(charities: List<com.example.project_ad_wallet.data.model.Charity>) {
        val randomDonations = (1..3).map { i ->
            val charity = charities.random()
            Donation(
                id = "rand_d_$i",
                charityId = charity.id,
                charityName = charity.name,
                amount = (1.0..5.0).random(),
                sdgNumber = charity.sdgNumber,
                sdgName = charity.sdgName,
                timestamp = System.currentTimeMillis() - (i * 7200000)
            )
        }
        
        repositoryScope.launch {
            randomDonations.forEach { donation ->
                donationsDao?.insertDonation(
                    DonationEntity(
                        charityId = donation.charityId,
                        charityName = donation.charityName,
                        amount = donation.amount,
                        timestamp = donation.timestamp
                    )
                )
                userProfileDao?.deductFromWalletBalance(donation.amount)
            }
        }
    }

    private fun ClosedRange<Double>.random() = 
        java.util.concurrent.ThreadLocalRandom.current().nextDouble(start, endInclusive)

    fun reset() {
        _donations.value = emptyList()
    }
}
