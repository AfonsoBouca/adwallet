package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.ui.adRating.AdRatingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdRatingViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var adRepo: AdRepository
    private lateinit var viewModel: AdRatingViewModel

    private val fakeAd = Ad(
        id = "test-ad",
        title = "Test Ad",
        description = "desc",
        thumbnailResId = 0,
        videoResId = 0,
        rewardAmount = 0.10,
        advertiserName = "TestCo",
        category = "Test",
    )

    @Before
    fun setup() {
        adRepo = AdRepository()
        adRepo.addAd(fakeAd)
        val adStatsDao = com.example.project_ad_wallet.fakes.FakeAdStatsDao()
        val adStatsRepository = com.example.project_ad_wallet.data.repository.AdStatsRepository(adStatsDao)
        viewModel = AdRatingViewModel(adRepo, adStatsRepository)
        viewModel.loadAd("test-ad")
    }

    @Test
    fun test_submitRatingSavesRatingToRepository() {
        viewModel.selectRating(4)
        viewModel.submitRating()

        assertEquals(1, adRepo.ratings.value.size)
        assertEquals("test-ad", adRepo.ratings.value.first().adId)
        assertEquals(4, adRepo.ratings.value.first().rating)
    }

    @Test
    fun test_submitButtonDisabledWithNoRating() {
        assertEquals(0, viewModel.uiState.value.selectedRating)
    }

    @Test
    fun test_skipDoesNotSaveRating() {
        viewModel.skipRating()

        assertTrue(adRepo.ratings.value.isEmpty())
        assertTrue(viewModel.uiState.value.isSkipped)
    }

    @Test
    fun test_ratingBoundaryZeroIsIgnored() {
        viewModel.selectRating(0)
        assertEquals(0, viewModel.uiState.value.selectedRating)
    }

    @Test
    fun test_ratingBoundaryFiveIsValid() {
        viewModel.selectRating(5)
        assertEquals(5, viewModel.uiState.value.selectedRating)
    }

    @Test
    fun test_ratingBoundarySixIsIgnored() {
        viewModel.selectRating(6)
        assertEquals(0, viewModel.uiState.value.selectedRating)
    }

    @Test
    fun test_submitSetsIsSubmittedTrue() {
        viewModel.selectRating(3)
        viewModel.submitRating()
        assertTrue(viewModel.uiState.value.isSubmitted)
    }
}
