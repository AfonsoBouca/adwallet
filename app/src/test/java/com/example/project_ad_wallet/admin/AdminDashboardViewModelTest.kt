package com.example.project_ad_wallet.admin

import com.example.project_ad_wallet.MainCoroutineRule
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import com.example.project_ad_wallet.fakes.FakeUserActivityRepository
import com.example.project_ad_wallet.ui.admin.AdminDashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminDashboardViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeRepository = FakeUserActivityRepository()
    private lateinit var viewModel: AdminDashboardViewModel

    @Before
    fun setup() {
        viewModel = AdminDashboardViewModel(fakeRepository)
    }

    @Test
    fun initialState_recentActivityIsEmpty() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.recentActivity.collect {} }
        assertTrue(viewModel.recentActivity.value.isEmpty())
    }

    @Test
    fun afterLoggingActivity_recentActivityIsNotEmpty() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.recentActivity.collect {} }
        fakeRepository.logActivity("User1", "AD_WATCHED")
        advanceUntilIdle()
        assertTrue(viewModel.recentActivity.value.isNotEmpty())
    }

    @Test
    fun flaggedCount_withFlaggedActivity_isCorrect() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.flaggedCount.collect {} }
        fakeRepository.logActivity("User1", "AD_WATCHED",
            isFlagged = true, flagReason = "Suspicious")
        advanceUntilIdle()
        assertEquals(1, viewModel.flaggedCount.value)
    }

    @Test
    fun flagActivity_marksActivityAsFlagged() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.recentActivity.collect {} }
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.flaggedActivity.collect {} }
        fakeRepository.logActivity("User1", "AD_WATCHED")
        advanceUntilIdle()
        val activityId = viewModel.recentActivity.value.first().id
        viewModel.flagActivity(activityId, "Manual flag")
        advanceUntilIdle()
        assertTrue(viewModel.flaggedActivity.value.isNotEmpty())
    }
}
