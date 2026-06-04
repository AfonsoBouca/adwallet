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
        viewModel.onDurationSecondsChanged("30")

        viewModel.onQuestionChanged("Is this a great product?")
        viewModel.onOptionChanged(0, "Yes")
        viewModel.onOptionChanged(1, "No")
        viewModel.onOptionChanged(2, "Maybe")
        viewModel.onOptionChanged(3, "I don't know")
        viewModel.onCorrectOptionSelected(0)
    }

    @Test
    fun test_submitWithAllValidFieldsCreatesAdAndQuestionnaire() {
        val initialCount = adRepo.ads.value.size
        fillValidForm()
        viewModel.submitAd()
        viewModel.confirmUpload()

        assertEquals(initialCount + 1, adRepo.ads.value.size)
        val newAd = adRepo.ads.value.last()
        assertEquals("Great Product", newAd.title)
        assertEquals(AdStatus.PENDING, newAd.status)
        
        val questions = adRepo.getQuestionsForAd(newAd.id)
        assertEquals(1, questions.size)
        assertEquals("Is this a great product?", questions[0].text)
        assertTrue(questions[0].options[0].isCorrect) // "Yes" was marked as correct
    }

    @Test
    fun test_submitWithEmptyTitleShowsTitleError() {
        fillValidForm()
        viewModel.onTitleChanged("") // Esvaziar o título de propósito
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.titleError)
        assertEquals(false, viewModel.uiState.value.showConfirmDialog)
    }

    @Test
    fun test_submitWithEmptyQuestionShowsQuestionError() {
        fillValidForm()
        viewModel.onQuestionChanged("") // Esvaziar a pergunta de propósito
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.questionError)
        assertEquals(false, viewModel.uiState.value.showConfirmDialog)
    }

    @Test
    fun test_submitWithEmptyOptionShowsOptionsError() {
        fillValidForm()
        viewModel.onOptionChanged(2, "") // Esvaziar a opção 3 de propósito
        viewModel.submitAd()

        assertNotNull(viewModel.uiState.value.optionsError)
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
    fun test_thumbnailCyclesThrough5Options() {
        val initial = viewModel.uiState.value.selectedThumbnailIndex
        repeat(5) { viewModel.cycleThumbnail() }
        assertEquals(initial, viewModel.uiState.value.selectedThumbnailIndex)
    }
}