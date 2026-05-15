package com.example.project_ad_wallet

import com.example.project_ad_wallet.ui.notifications.NotificationPrefsViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationPrefsViewModelTest {

    private lateinit var viewModel: NotificationPrefsViewModel

    @Before
    fun setup() {
        viewModel = NotificationPrefsViewModel()
    }

    @Test
    fun test_defaultNewAdsEnabledIsTrue() {
        assertTrue(viewModel.uiState.value.newAdsEnabled)
    }

    @Test
    fun test_defaultEarningsEnabledIsTrue() {
        assertTrue(viewModel.uiState.value.earningsEnabled)
    }

    @Test
    fun test_defaultDonationRemindersEnabledIsFalse() {
        assertFalse(viewModel.uiState.value.donationRemindersEnabled)
    }

    @Test
    fun test_defaultWeeklyDigestEnabledIsFalse() {
        assertFalse(viewModel.uiState.value.weeklyDigestEnabled)
    }

    @Test
    fun test_setNewAdsEnabledFalseUpdatesState() {
        viewModel.setNewAdsEnabled(false)
        assertFalse(viewModel.uiState.value.newAdsEnabled)
    }

    @Test
    fun test_setDonationRemindersEnabledTrueUpdatesState() {
        viewModel.setDonationRemindersEnabled(true)
        assertTrue(viewModel.uiState.value.donationRemindersEnabled)
    }

    @Test
    fun test_setWeeklyDigestEnabledTrueUpdatesState() {
        viewModel.setWeeklyDigestEnabled(true)
        assertTrue(viewModel.uiState.value.weeklyDigestEnabled)
    }

    @Test
    fun test_toggleMultiplePrefsIndependently() {
        viewModel.setNewAdsEnabled(false)
        viewModel.setEarningsEnabled(false)
        viewModel.setDonationRemindersEnabled(true)
        assertFalse(viewModel.uiState.value.newAdsEnabled)
        assertFalse(viewModel.uiState.value.earningsEnabled)
        assertTrue(viewModel.uiState.value.donationRemindersEnabled)
        assertTrue(viewModel.uiState.value.earningsEnabled.not())
    }
}
