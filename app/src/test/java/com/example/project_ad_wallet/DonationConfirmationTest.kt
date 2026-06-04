package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.repository.CharityRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.ui.charities.CharitiesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DonationConfirmationTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var walletRepo: WalletRepository
    private lateinit var charityRepo: CharityRepository
    private lateinit var donationRepo: DonationRepository
    private lateinit var viewModel: CharitiesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        walletRepo = WalletRepository(externalScope = testScope)
        charityRepo = CharityRepository()
        donationRepo = DonationRepository(externalScope = testScope)
        viewModel = CharitiesViewModel(charityRepo, walletRepo, donationRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun addFunds(amount: Double) {
        walletRepo.addEarning(
            Earning(id = "seed", adId = "seed", adTitle = "Seed", amount = amount)
        )
    }

    @Test
    fun test_successfulDonationDeductsBalanceAndSavesEntry() {
        addFunds(5.00)
        val charity = charityRepo.getCharityById("zero-hunger-foundation")!!
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("2.00")
        viewModel.requestDonation()
        viewModel.confirmDonation()

        assertEquals(3.00, walletRepo.balance.value, 0.001)
        assertEquals(1, donationRepo.donations.value.size)
        assertEquals("zero-hunger-foundation", donationRepo.donations.value.first().charityId)
        assertEquals(2.00, donationRepo.donations.value.first().amount, 0.001)
    }

    @Test
    fun test_insufficientBalanceDoesNotDeductAndShowsError() {
        // balance = 0 (no funds added)
        val charity = charityRepo.getCharityById("green-earth")!!
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("5.00")
        viewModel.requestDonation()

        // requestDonation should set error when amount > balance
        assertNotNull(viewModel.uiState.value.errorMessage)
        assertEquals(0.0, walletRepo.balance.value, 0.001)
        assertTrue(donationRepo.donations.value.isEmpty())
    }

    @Test
    fun test_cancelledDonationDoesNothing() {
        addFunds(5.00)
        val charity = charityRepo.getCharityById("green-earth")!!
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("2.00")
        viewModel.cancelDonation()

        assertEquals(5.00, walletRepo.balance.value, 0.001)
        assertTrue(donationRepo.donations.value.isEmpty())
    }

    @Test
    fun test_donationEntryHasCorrectTimestamp() {
        addFunds(10.00)
        val before = System.currentTimeMillis()
        val charity = charityRepo.getCharityById("clean-water-initiative")!!
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("3.00")
        viewModel.requestDonation()
        viewModel.confirmDonation()
        val after = System.currentTimeMillis()

        val donation = donationRepo.donations.value.firstOrNull()
        assertNotNull(donation)
        assertTrue(donation!!.timestamp in before..after)
    }

    @Test
    fun test_donationEntryHasCharityName() {
        addFunds(5.00)
        val charity = charityRepo.getCharityById("green-earth")!!
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("1.00")
        viewModel.requestDonation()
        viewModel.confirmDonation()

        val donation = donationRepo.donations.value.firstOrNull()
        assertEquals("Green Earth", donation?.charityName)
    }
}
