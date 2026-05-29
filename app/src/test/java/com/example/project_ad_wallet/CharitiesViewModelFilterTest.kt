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
}