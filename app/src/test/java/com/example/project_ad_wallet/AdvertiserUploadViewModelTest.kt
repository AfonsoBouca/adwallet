package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.ui.advertiserUpload.AdvertiserUploadViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdvertiserUploadViewModelTest {

    private lateinit var adRepo: AdRepository
    private lateinit var viewModel: AdvertiserUploadViewModel

    @Before
    fun setup() {
        adRepo = AdRepository()
        viewModel = AdvertiserUploadViewModel(adRepo)
    }

    private fun fillValidForm() {
        viewModel.onTitleChanged("Great Product")
        viewModel.onCompanyNameChanged("TestCo")
        viewModel.onCategoryChanged("Education")
        viewModel.onDescriptionChanged("A great product for everyone.")
        viewModel.onRewardAmountChanged("0.25")
    }

    @Test
    fun test_submitWithAllValidFieldsCreatesAd() {
        val initialCount = adRepo.ads.value.size
        fillValidForm()
        viewModel.submitAd()
        viewModel.confirmUpload()

        assertEquals(initialCount + 1, adRepo.ads.value.size)
        val newAd = adRepo.ads.value.last()
        assertEquals("Great Product", newAd.title)
        assertEquals(AdStatus.PENDING, newAd.status) // <-- Alterado para o novo Enum!
    }

    @Test
    fun test_submitWithEmptyTitleShowsTitleError() {
        viewModel.onCompanyNameChanged("TestCo")
        viewModel.onCategoryChanged("Education")
        viewModel.onDescriptionChanged("A great product.")
        viewModel.onRewardAmountChanged("0.25")
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.titleError)
        assertEquals(false, viewModel.uiState.value.showConfirmDialog)
    }

    @Test
    fun test_submitWithInvalidRewardShowsRewardError_aboveMax() {
        fillValidForm()
        viewModel.onRewardAmountChanged("5.00")
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.rewardAmountError)
    }

    @Test
    fun test_submitWithValidRewardBelowMinShowsError() {
        fillValidForm()
        viewModel.onRewardAmountChanged("0.00")
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.rewardAmountError)
    }

    @Test
    fun test_cancelledSubmissionDoesNotCreateAd() {
        val initialCount = adRepo.ads.value.size
        fillValidForm()
        viewModel.cancelUpload()

        assertEquals(initialCount, adRepo.ads.value.size)
        assertEquals("", viewModel.uiState.value.title)
    }

    @Test
    fun test_adIsSubmittedWithStatusPending() {
        fillValidForm()
        viewModel.submitAd()
        viewModel.confirmUpload()

        val newAd = adRepo.ads.value.last()
        assertEquals(AdStatus.PENDING, newAd.status) // <-- Alterado para o novo Enum!
    }

    @Test
    fun test_thumbnailCyclesThrough5Options() {
        val initial = viewModel.uiState.value.selectedThumbnailIndex
        repeat(5) { viewModel.cycleThumbnail() }
        assertEquals(initial, viewModel.uiState.value.selectedThumbnailIndex)
    }
}