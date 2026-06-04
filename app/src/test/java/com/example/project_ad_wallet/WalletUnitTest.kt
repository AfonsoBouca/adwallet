package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import com.example.project_ad_wallet.ui.wallet.WalletViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WalletUnitTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var walletRepo: WalletRepository
    private lateinit var donationRepo: DonationRepository
    private lateinit var withdrawalRepo: WithdrawalRepository
    private lateinit var userRepo: UserRepository
    private lateinit var viewModel: WalletViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        walletRepo = WalletRepository(externalScope = testScope)
        donationRepo = DonationRepository(externalScope = testScope)
        withdrawalRepo = WithdrawalRepository()
        userRepo = UserRepository(externalScope = testScope)
        viewModel = WalletViewModel(walletRepo, donationRepo, withdrawalRepo, userRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_initialBalanceIsZero() {
        assertEquals(0.0, viewModel.uiState.value.balance, 0.001)
    }

    @Test
    fun test_addEarningUpdatesBalance() {
        walletRepo.addEarning(Earning(id = "e1", adId = "1", adTitle = "Ad", amount = 0.50))
        assertEquals(0.50, viewModel.uiState.value.balance, 0.001)
    }

    @Test
    fun test_addFundsUpdatesBalance() {
        walletRepo.addFunds(100.0)
        assertEquals(100.0, viewModel.uiState.value.balance, 0.001)
    }
}
