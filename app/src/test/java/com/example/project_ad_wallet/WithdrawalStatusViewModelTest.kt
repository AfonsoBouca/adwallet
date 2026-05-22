package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.model.WithdrawalStatus
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalStatusViewModel
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
class WithdrawalStatusViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var withdrawalRepo: WithdrawalRepository
    private lateinit var viewModel: WithdrawalStatusViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        withdrawalRepo = WithdrawalRepository()
        viewModel = WithdrawalStatusViewModel(withdrawalRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_emptyStateWhenNoWithdrawals() {
        assertTrue(viewModel.uiState.value.isEmpty)
        assertTrue(viewModel.uiState.value.withdrawals.isEmpty())
    }

    @Test
    fun test_totalWithdrawnIsZeroWithNoWithdrawals() {
        assertEquals(0.0, viewModel.uiState.value.totalWithdrawn, 0.001)
    }

    @Test
    fun test_pendingCountIsZeroWithNoWithdrawals() {
        assertEquals(0, viewModel.uiState.value.pendingCount)
    }

    @Test
    fun test_addWithdrawalUpdatesState() {
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w1", amount = 5.0, status = WithdrawalStatus.PENDING)
        )
        assertEquals(1, viewModel.uiState.value.withdrawals.size)
        assertTrue(!viewModel.uiState.value.isEmpty)
    }

    @Test
    fun test_totalWithdrawnOnlyCountsCompleted() {
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w1", amount = 5.0, status = WithdrawalStatus.COMPLETED)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w2", amount = 3.0, status = WithdrawalStatus.PENDING)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w3", amount = 2.0, status = WithdrawalStatus.REJECTED)
        )
        assertEquals(5.0, viewModel.uiState.value.totalWithdrawn, 0.001)
    }

    @Test
    fun test_pendingCountIncludesPendingAndProcessing() {
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w1", amount = 5.0, status = WithdrawalStatus.PENDING)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w2", amount = 3.0, status = WithdrawalStatus.PROCESSING)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w3", amount = 2.0, status = WithdrawalStatus.COMPLETED)
        )
        assertEquals(2, viewModel.uiState.value.pendingCount)
    }

    @Test
    fun test_withdrawalsAreSortedByTimestampDescending() {
        val now = System.currentTimeMillis()
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w1", amount = 1.0, timestamp = now - 10_000)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w2", amount = 2.0, timestamp = now - 5_000)
        )
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w3", amount = 3.0, timestamp = now)
        )

        val withdrawals = viewModel.uiState.value.withdrawals
        assertEquals(3, withdrawals.size)
        assertEquals("w3", withdrawals[0].id)
        assertEquals("w2", withdrawals[1].id)
        assertEquals("w1", withdrawals[2].id)
    }

    @Test
    fun test_updateStatusChangesWithdrawalStatus() {
        withdrawalRepo.addWithdrawal(
            WithdrawalRequest(id = "w1", amount = 5.0, status = WithdrawalStatus.PENDING)
        )
        withdrawalRepo.updateStatus("w1", WithdrawalStatus.COMPLETED)
        assertEquals(WithdrawalStatus.COMPLETED, viewModel.uiState.value.withdrawals[0].status)
        assertEquals(5.0, viewModel.uiState.value.totalWithdrawn, 0.001)
    }
}
