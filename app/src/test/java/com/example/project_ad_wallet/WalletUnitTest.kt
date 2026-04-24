package com.example.project_ad_wallet

import com.example.project_ad_wallet.domain.model.ThemeMode
import com.example.project_ad_wallet.ui.viewmodel.WalletViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WalletUnitTest {

    @Test
    fun `wallet initial balance is correct`() {
        val viewModel = WalletViewModel()

        assertEquals(15.00, viewModel.uiState.value.balance, 0.001)
        assertEquals(ThemeMode.System, viewModel.uiState.value.settings.themeMode)
    }

    @Test
    fun `claiming a survey reward updates the wallet balance`() {
        val viewModel = WalletViewModel()

        viewModel.claimSurveyReward("nike-running-shoes")

        assertEquals(15.50, viewModel.uiState.value.balance, 0.001)
    }

    @Test
    fun `valid donation deducts balance and creates success banner`() {
        val viewModel = WalletViewModel()

        val success = viewModel.donateToCharity("green-earth", 5.00)

        assertTrue(success)
        assertEquals(10.00, viewModel.uiState.value.balance, 0.001)
        assertEquals(
            "Thank you for supporting SDG 13!",
            viewModel.uiState.value.donationBanner?.message
        )
    }
}
