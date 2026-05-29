package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.data.repository.CharityRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.ui.charities.CharitiesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import com.example.project_ad_wallet.data.model.Donation
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharitiesViewModelFilterTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var charityRepo: CharityRepository
    private lateinit var walletRepo: WalletRepository
    private lateinit var donationRepo: DonationRepository
    private lateinit var viewModel: CharitiesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        charityRepo = CharityRepository()
        walletRepo = WalletRepository()
        donationRepo = DonationRepository()
        viewModel = CharitiesViewModel(charityRepo, walletRepo, donationRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_usersOnlySeeVerifiedCharities() {
        val visibleCharities = viewModel.uiState.value.charities
        
        val hasPending = visibleCharities.any { it.status == CharityStatus.PENDING }
        assertFalse("A UI não pode mostrar instituições pendentes", hasPending)

        val hasVerified = visibleCharities.any { it.status == CharityStatus.VERIFIED }
        assertTrue("A UI deve mostrar as instituições verificadas", hasVerified)
    }

    @Test
    fun test_donationTotalsAreAggregatedCorrectly() {
        val d1 = Donation(id = "1", charityId = "c-1", charityName = "C1", amount = 10.0, sdgNumber = 1, sdgName = "N1")
        val d2 = Donation(id = "2", charityId = "c-1", charityName = "C1", amount = 15.0, sdgNumber = 1, sdgName = "N1")
        val d3 = Donation(id = "3", charityId = "c-2", charityName = "C2", amount = 5.0, sdgNumber = 2, sdgName = "N2")

        donationRepo.addDonation(d1)
        donationRepo.addDonation(d2)
        donationRepo.addDonation(d3)

        val donationsState = viewModel.uiState.value.charityDonations
        assertEquals(25.0, donationsState["c-1"] ?: 0.0, 0.001)
        assertEquals(5.0, donationsState["c-2"] ?: 0.0, 0.001)
    }

    @Test
    fun test_makingDonationUpdatesAggregatedTotals() {
        val charity = charityRepo.getCharityById("zero-hunger-foundation")!!
        walletRepo.addFunds(20.0)

        // Select charity and confirm donation
        viewModel.selectCharity(charity)
        viewModel.onAmountChanged("8.50")
        viewModel.confirmDonation()

        val donationsState = viewModel.uiState.value.charityDonations
        assertEquals(8.50, donationsState["zero-hunger-foundation"] ?: 0.0, 0.001)
    }
}