package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.ui.adWatch.AdReportViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdReportViewModelTest {

    private lateinit var adRepo: AdRepository
    private lateinit var viewModel: AdReportViewModel

    @Before
    fun setup() {
        adRepo = AdRepository()
        viewModel = AdReportViewModel(adRepo)
    }

    @Test
    fun test_initialStateHasNoReasonAndNotSubmitted() {
        assertNull(viewModel.uiState.value.selectedReason)
        assertFalse(viewModel.uiState.value.isSubmitted)
        assertTrue(viewModel.uiState.value.comment.isEmpty())
    }

    @Test
    fun test_selectReasonUpdatesState() {
        viewModel.selectReason("Misleading content")
        assertEquals("Misleading content", viewModel.uiState.value.selectedReason)
    }

    @Test
    fun test_commentChangedUpdatesState() {
        viewModel.onCommentChanged("This ad is fake.")
        assertEquals("This ad is fake.", viewModel.uiState.value.comment)
    }

    @Test
    fun test_submitReportWithoutReasonDoesNothing() {
        // No reason selected, submitReport should be a no-op
        viewModel.submitReport("ad-id-1")
        assertFalse(viewModel.uiState.value.isSubmitted)
        assertTrue(adRepo.reports.value.isEmpty())
    }

    @Test
    fun test_submitReportWithReasonSavesAndSetsSubmitted() {
        viewModel.selectReason("Spam or repetitive")
        viewModel.submitReport("ad-id-1")
        assertTrue(viewModel.uiState.value.isSubmitted)
        assertEquals(1, adRepo.reports.value.size)
        assertEquals("ad-id-1", adRepo.reports.value.first().adId)
        assertEquals("Spam or repetitive", adRepo.reports.value.first().reason)
    }

    @Test
    fun test_submitReportIncludesComment() {
        viewModel.selectReason("Other")
        viewModel.onCommentChanged("Seen this 10 times today.")
        viewModel.submitReport("ad-id-2")
        assertEquals("Seen this 10 times today.", adRepo.reports.value.first().comment)
    }

    @Test
    fun test_resetClearsState() {
        viewModel.selectReason("Misleading content")
        viewModel.onCommentChanged("Some comment")
        viewModel.submitReport("ad-id-3")
        viewModel.reset()
        assertNull(viewModel.uiState.value.selectedReason)
        assertFalse(viewModel.uiState.value.isSubmitted)
        assertTrue(viewModel.uiState.value.comment.isEmpty())
    }
}
