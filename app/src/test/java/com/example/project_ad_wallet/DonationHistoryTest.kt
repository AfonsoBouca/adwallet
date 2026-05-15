package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.ui.donations.DonationHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DonationHistoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var donationRepo: DonationRepository
    private lateinit var viewModel: DonationHistoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        donationRepo = DonationRepository()
        viewModel = DonationHistoryViewModel(donationRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun makeDonation(id: String, charityId: String, charityName: String, amount: Double, timestamp: Long = System.currentTimeMillis()) =
        Donation(
            id = id,
            charityId = charityId,
            charityName = charityName,
            amount = amount,
            sdgNumber = 1,
            sdgName = "Test SDG",
            timestamp = timestamp
        )

    @Test
    fun test_isEmptyWhenNoDonations() {
        assertTrue(viewModel.uiState.value.isEmpty)
        assertTrue(viewModel.uiState.value.donations.isEmpty())
    }

    @Test
    fun test_isNotEmptyAfterDonation() {
        donationRepo.addDonation(makeDonation("d1", "charity-1", "Green Earth", 2.00))
        assertTrue(!viewModel.uiState.value.isEmpty)
    }

    @Test
    fun test_totalDonatedIsCorrectSum() {
        donationRepo.addDonation(makeDonation("d1", "charity-1", "Green Earth", 3.00))
        donationRepo.addDonation(makeDonation("d2", "charity-2", "Zero Hunger", 2.00))
        assertEquals(5.00, viewModel.uiState.value.totalDonated, 0.001)
    }

    @Test
    fun test_donationsAreSortedMostRecentFirst() {
        val old = makeDonation("d1", "charity-1", "Green Earth", 1.00, timestamp = 1000L)
        val recent = makeDonation("d2", "charity-2", "Zero Hunger", 2.00, timestamp = 9000L)
        donationRepo.addDonation(old)
        donationRepo.addDonation(recent)
        val donations = viewModel.uiState.value.donations
        assertEquals("d2", donations.first().id)
        assertEquals("d1", donations.last().id)
    }

    @Test
    fun test_donationCountMatchesAdded() {
        donationRepo.addDonation(makeDonation("d1", "charity-1", "Green Earth", 1.00))
        donationRepo.addDonation(makeDonation("d2", "charity-2", "Zero Hunger", 2.00))
        donationRepo.addDonation(makeDonation("d3", "charity-3", "Clean Water", 3.00))
        assertEquals(3, viewModel.uiState.value.donations.size)
    }
}
