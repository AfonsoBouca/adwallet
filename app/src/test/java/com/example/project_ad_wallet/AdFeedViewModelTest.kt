package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.ui.adFeed.AdFeedViewModel
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
class AdFeedViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var adRepo: AdRepository
    private lateinit var walletRepo: WalletRepository
    private lateinit var viewModel: AdFeedViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        adRepo = AdRepository()
        walletRepo = WalletRepository()
        viewModel = AdFeedViewModel(adRepo, walletRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_feedOnlyShowsApprovedAds() {
        val approvedAd = Ad(id = "test-1", title = "A", description = "D", thumbnailResId = 0, videoResId = 0, rewardAmount = 0.1, advertiserName = "A", category = "C", status = AdStatus.APPROVED)
        val pendingAd = Ad(id = "test-2", title = "P", description = "D", thumbnailResId = 0, videoResId = 0, rewardAmount = 0.1, advertiserName = "A", category = "C", status = AdStatus.PENDING)
        val rejectedAd = Ad(id = "test-3", title = "R", description = "D", thumbnailResId = 0, videoResId = 0, rewardAmount = 0.1, advertiserName = "A", category = "C", status = AdStatus.REJECTED)

        adRepo.addAd(approvedAd)
        adRepo.addAd(pendingAd)
        adRepo.addAd(rejectedAd)

        val visibleAds = viewModel.uiState.value.ads

        val hasPending = visibleAds.any { it.id == "test-2" }
        val hasRejected = visibleAds.any { it.id == "test-3" }
        val hasApproved = visibleAds.any { it.id == "test-1" }

        assertTrue(hasApproved)
        assertEquals(false, hasPending)
        assertEquals(false, hasRejected)
    }
}