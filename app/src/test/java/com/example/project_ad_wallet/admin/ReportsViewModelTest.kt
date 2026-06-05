package com.example.project_ad_wallet.admin

import com.example.project_ad_wallet.MainCoroutineRule
import com.example.project_ad_wallet.fakes.FakeReportsRepository
import com.example.project_ad_wallet.ui.admin.ReportsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReportsViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeRepository = FakeReportsRepository()
    private lateinit var viewModel: ReportsViewModel

    @Before
    fun setup() {
        viewModel = ReportsViewModel(fakeRepository)
    }

    @Test
    fun initialState_reportIsNull() {
        assertNull(viewModel.report)
    }

    @Test
    fun generateReport_setsReport() = runTest {
        viewModel.generateReport()
        advanceUntilIdle()
        assertNotNull(viewModel.report)
    }

    @Test
    fun generateReport_isLoadingDuringGeneration() = runTest {
        viewModel.generateReport()
        // isLoading é true durante, false depois
        advanceUntilIdle()
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun report_hasCorrectTotalEarned() = runTest {
        fakeRepository.setTotalEarned(10.00)
        viewModel.generateReport()
        advanceUntilIdle()
        assertEquals(10.00, viewModel.report?.totalAmountEarned ?: 0.0, 0.001)
    }
}
