package com.example.project_ad_wallet

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.project_ad_wallet.ui.notifications.NotificationPrefsViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationPrefsViewModelTest {

    private lateinit var viewModel: NotificationPrefsViewModel

    @Before
    fun setup() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = NotificationPrefsViewModel(app)
    }

    @Test
    fun test_defaultValues() {
        assertTrue(viewModel.uiState.value.masterEnabled)
        assertTrue(viewModel.uiState.value.newAdsEnabled)
        assertTrue(viewModel.uiState.value.earningsEnabled)
        assertTrue(viewModel.uiState.value.donationRemindersEnabled)
        assertFalse(viewModel.uiState.value.weeklyDigestEnabled)
        assertFalse(viewModel.uiState.value.promotionsEnabled)
    }

    @Test
    fun test_masterOffDisablesAllInUiState() {
        viewModel.setMasterEnabled(false)
        val state = viewModel.uiState.value
        assertFalse(state.masterEnabled)
        assertFalse(state.newAdsEnabled)
        assertFalse(state.earningsEnabled)
        assertFalse(state.donationRemindersEnabled)
        assertFalse(state.weeklyDigestEnabled)
        assertFalse(state.promotionsEnabled)
    }

    @Test
    fun test_masterOnRestoresIndividualToggles() {
        // Given
        viewModel.setWeeklyDigestEnabled(true)
        assertTrue(viewModel.uiState.value.weeklyDigestEnabled)
        
        // When
        viewModel.setMasterEnabled(false)
        assertFalse(viewModel.uiState.value.weeklyDigestEnabled)
        
        // And
        viewModel.setMasterEnabled(true)
        
        // Then
        assertTrue(viewModel.uiState.value.weeklyDigestEnabled)
    }

    @Test
    fun test_setIndividualPrefWhenMasterOffDoesNothing() {
        viewModel.setMasterEnabled(false)
        viewModel.setPromotionsEnabled(true)
        assertFalse(viewModel.uiState.value.promotionsEnabled)
    }

    @Test
    fun test_toggleMultiplePrefsIndependently() {
        viewModel.setNewAdsEnabled(false)
        viewModel.setPromotionsEnabled(true)
        assertFalse(viewModel.uiState.value.newAdsEnabled)
        assertTrue(viewModel.uiState.value.promotionsEnabled)
        assertTrue(viewModel.uiState.value.earningsEnabled) // still true
    }
}
