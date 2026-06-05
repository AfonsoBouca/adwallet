package com.example.project_ad_wallet.wallet

import com.example.project_ad_wallet.MainCoroutineRule
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.ui.wallet.EarningsHistoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FakeWalletRepository : WalletRepository() {
    private val _earningsList = MutableStateFlow<List<EarningEntity>>(emptyList())

    override fun getAllEarnings(): Flow<List<EarningEntity>> = _earningsList

    override fun getTotalEarned(): Flow<Double> = _earningsList.map { list -> list.sumOf { it.amount } }

    fun addEarning(earning: EarningEntity) {
        _earningsList.update { list -> (list + earning).sortedByDescending { it.timestamp } }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class EarningsHistoryViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeRepository = FakeWalletRepository()
    private lateinit var viewModel: EarningsHistoryViewModel

    @Before
    fun setup() {
        viewModel = EarningsHistoryViewModel(fakeRepository)
    }

    @Test
    fun initialState_earningsIsEmpty() = runTest {
        assertTrue(viewModel.earnings.value.isEmpty())
    }

    @Test
    fun afterAddingEarning_earningsIsNotEmpty() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.earnings.collect {}
        }
        fakeRepository.addEarning(
            EarningEntity(adTitle = "Test Ad", adId = "1", amount = 0.50)
        )
        advanceUntilIdle()
        assertTrue(viewModel.earnings.value.isNotEmpty())
        collectJob.cancel()
    }

    @Test
    fun totalEarned_withMultipleEarnings_isCorrect() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.totalEarned.collect {}
        }
        fakeRepository.addEarning(
            EarningEntity(adTitle = "Ad 1", adId = "1", amount = 1.00)
        )
        fakeRepository.addEarning(
            EarningEntity(adTitle = "Ad 2", adId = "2", amount = 2.00)
        )
        advanceUntilIdle()
        assertEquals(3.00, viewModel.totalEarned.value, 0.001)
        collectJob.cancel()
    }

    @Test
    fun earnings_areOrderedByTimestampDescending() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.earnings.collect {}
        }
        fakeRepository.addEarning(
            EarningEntity(adTitle = "Old Ad", adId = "1",
                amount = 1.00, timestamp = 1000L)
        )
        fakeRepository.addEarning(
            EarningEntity(adTitle = "New Ad", adId = "2",
                amount = 1.00, timestamp = 9000L)
        )
        advanceUntilIdle()
        assertEquals("New Ad", viewModel.earnings.value.first().adTitle)
        collectJob.cancel()
    }
}
