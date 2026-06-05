package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WithdrawalRequestViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var walletRepository: WalletRepository
    private lateinit var withdrawalRepository: WithdrawalRepository
    private lateinit var viewModel: WithdrawalRequestViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        walletRepository = WalletRepository(externalScope = testScope)
        withdrawalRepository = WithdrawalRepository()
        viewModel = WithdrawalRequestViewModel(walletRepository, withdrawalRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_requestWithdrawalFailsWhenAmountIsInvalid() {
        viewModel.requestWithdrawal("abc", "PT50000000000000000000000")

        assertFalse(viewModel.uiState.value.requestSent)
        assertEquals("Enter a valid amount", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun test_requestWithdrawalFailsWhenBankAccountMissing() {
        walletRepository.addFunds(10.0)
        viewModel.requestWithdrawal("5.00", "")

        assertFalse(viewModel.uiState.value.requestSent)
        assertEquals("Bank account number is required", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun test_requestWithdrawalFailsWhenBalanceIsInsufficient() {
        walletRepository.addFunds(2.0)
        viewModel.requestWithdrawal("5.00", "PT50000000000000000000000")

        assertFalse(viewModel.uiState.value.requestSent)
        assertEquals("Insufficient balance. Earn more before withdrawing.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun test_requestWithdrawalSucceedsAndCreatesPendingWithdrawal() {
        walletRepository.addFunds(20.0)
        viewModel.requestWithdrawal("10.00", "PT50000000000000000000000")

        assertTrue(viewModel.uiState.value.requestSent)
        assertEquals(null, viewModel.uiState.value.errorMessage)
        assertEquals(10.0, walletRepository.balance.value, 0.001)
        assertEquals(1, withdrawalRepository.withdrawals.value.size)
        assertEquals(10.0, withdrawalRepository.withdrawals.value.first().amount, 0.001)
        assertEquals("PT50000000000000000000000", withdrawalRepository.withdrawals.value.first().bankAccount)
    }
}
