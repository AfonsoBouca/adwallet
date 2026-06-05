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
import org.junit.Assert.assertFalse
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
        adRepo = AdRepository(externalScope = kotlinx.coroutines.CoroutineScope(testDispatcher))
        walletRepo = WalletRepository(externalScope = kotlinx.coroutines.CoroutineScope(testDispatcher))
        val userRepository = com.example.project_ad_wallet.data.repository.UserRepository(externalScope = kotlinx.coroutines.CoroutineScope(testDispatcher))
        viewModel = AdFeedViewModel(adRepo, walletRepo, userRepository)
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

    @Test
    fun test_feedHidesSkippedAds() {
        val approvedAd1 = Ad(id = "test-1", title = "A", description = "D", thumbnailResId = 0, videoResId = 0, rewardAmount = 0.1, advertiserName = "A", category = "C", status = AdStatus.APPROVED)
        val approvedAd2 = Ad(id = "test-2", title = "B", description = "D", thumbnailResId = 0, videoResId = 0, rewardAmount = 0.1, advertiserName = "A", category = "C", status = AdStatus.APPROVED)

        adRepo.addAd(approvedAd1)
        adRepo.addAd(approvedAd2)

        // Skip one of the ads
        adRepo.markSkipped("test-1")

        val visibleAds = viewModel.uiState.value.ads
        val hasAd1 = visibleAds.any { it.id == "test-1" }
        val hasAd2 = visibleAds.any { it.id == "test-2" }

        assertFalse(hasAd1)
        assertTrue(hasAd2)
    }
}