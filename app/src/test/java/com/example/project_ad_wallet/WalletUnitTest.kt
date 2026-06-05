package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.fakes.FakeEarningsDao
import com.example.project_ad_wallet.fakes.FakeUserProfileDao
import com.example.project_ad_wallet.ui.wallet.WalletViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WalletUnitTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var walletRepo: WalletRepository
    private lateinit var donationRepo: DonationRepository
    private lateinit var viewModel: WalletViewModel
    private lateinit var userProfileDao: FakeUserProfileDao
    private lateinit var earningsDao: FakeEarningsDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userProfileDao = FakeUserProfileDao()
        earningsDao = FakeEarningsDao()
        walletRepo = WalletRepository(earningsDao, userProfileDao, kotlinx.coroutines.CoroutineScope(testDispatcher))

        donationRepo = DonationRepository()
        viewModel = WalletViewModel(walletRepo, donationRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_initialBalanceIsZero() = runTest {
        assertEquals(0.0, viewModel.uiState.value.balance, 0.001)
    }

    @Test
    fun test_addEarningUpdatesBalance() = runTest {
        walletRepo.addEarning(Earning(id = "e1", adId = "1", adTitle = "Ad", amount = 0.50))
        assertEquals(0.50, viewModel.uiState.value.balance, 0.001)
    }

    @Test
    fun test_addFundsUpdatesBalance() = runTest {
        walletRepo.addFunds(100.0)
        assertEquals(100.0, viewModel.uiState.value.balance, 0.001)
    }
}
