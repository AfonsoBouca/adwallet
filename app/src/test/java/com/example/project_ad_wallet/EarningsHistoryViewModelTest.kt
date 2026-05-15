package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.ui.earnings.EarningsHistoryViewModel
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
class EarningsHistoryViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var walletRepo: WalletRepository
    private lateinit var viewModel: EarningsHistoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        walletRepo = WalletRepository()
        viewModel = EarningsHistoryViewModel(walletRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_emptyStateWhenNoEarnings() {
        assertTrue(viewModel.uiState.value.isEmpty)
        assertTrue(viewModel.uiState.value.earnings.isEmpty())
    }

    @Test
    fun test_totalEarnedIsZeroWithNoEarnings() {
        assertEquals(0.0, viewModel.uiState.value.totalEarned, 0.001)
    }

    @Test
    fun test_earningsAreDisplayedMostRecentFirst() {
        val now = System.currentTimeMillis()
        walletRepo.addEarning(Earning("e1", "1", "Ad A", 0.10, now - 10_000))
        walletRepo.addEarning(Earning("e2", "2", "Ad B", 0.15, now - 5_000))
        walletRepo.addEarning(Earning("e3", "3", "Ad C", 0.20, now))

        val earnings = viewModel.uiState.value.earnings
        assertEquals(3, earnings.size)
        assertEquals("e3", earnings[0].id)
        assertEquals("e2", earnings[1].id)
        assertEquals("e1", earnings[2].id)
    }

    @Test
    fun test_totalEarnedIsCorrectSum() {
        walletRepo.addEarning(Earning("e1", "1", "Ad A", 0.10))
        walletRepo.addEarning(Earning("e2", "2", "Ad B", 0.15))
        walletRepo.addEarning(Earning("e3", "3", "Ad C", 0.20))

        assertEquals(0.45, viewModel.uiState.value.totalEarned, 0.001)
    }

    @Test
    fun test_isEmptyFalseAfterEarning() {
        walletRepo.addEarning(Earning("e1", "1", "Ad A", 0.10))
        assertTrue(!viewModel.uiState.value.isEmpty)
    }
}
