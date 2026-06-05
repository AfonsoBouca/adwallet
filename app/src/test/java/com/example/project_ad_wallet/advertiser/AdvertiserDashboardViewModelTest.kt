package com.example.project_ad_wallet.advertiser

import com.example.project_ad_wallet.MainCoroutineRule
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import com.example.project_ad_wallet.fakes.FakeAdStatsRepository
import com.example.project_ad_wallet.ui.advertiser.AdvertiserDashboardViewModel
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
class AdvertiserDashboardViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeRepository = FakeAdStatsRepository()
    private lateinit var viewModel: AdvertiserDashboardViewModel

    @Before
    fun setup() {
        viewModel = AdvertiserDashboardViewModel(fakeRepository, com.example.project_ad_wallet.data.repository.AdRepository())
    }

    @Test
    fun initialState_adStatsIsEmpty() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.adStats.collect {} }
        assertTrue(viewModel.adStats.value.isEmpty())
    }

    @Test
    fun totalViews_withData_isCorrect() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.totalViews.collect {} }
        fakeRepository.addStats(
            AdStatsEntity(adId = "1", adTitle = "Ad 1", totalViews = 10)
        )
        advanceUntilIdle()
        assertEquals(10, viewModel.totalViews.value)
    }

    @Test
    fun completionRate_withViewsAndCompletions_isCorrect() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.completionRate.collect {} }
        fakeRepository.addStats(
            AdStatsEntity(
                adId = "1", adTitle = "Ad 1",
                totalViews = 10, totalCompletions = 5
            )
        )
        advanceUntilIdle()
        assertEquals(50.0, viewModel.completionRate.value, 0.1)
    }

    @Test
    fun completionRate_withNoViews_isZero() = runTest {
        backgroundScope.launch(coroutineRule.dispatcher) { viewModel.completionRate.collect {} }
        advanceUntilIdle()
        assertEquals(0.0, viewModel.completionRate.value, 0.001)
    }
}
