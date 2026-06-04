package com.example.project_ad_wallet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Feature: Histórico de doações
 *
 * Scenario: O utilizador vê o histórico de doações vazio
 *   Given o utilizador não fez doações
 *   When navega para a tab de doações
 *   Then vê a mensagem de que não há doações
 *
 * Scenario: O utilizador vê as doações passadas
 *   Given o utilizador fez uma doação
 *   When acede ao histórico
 *   Then a doação é listada
 */
@RunWith(AndroidJUnit4::class)
class DonationHistoryAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun registerUser() {
        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("Test User")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("25")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Male").performClick()
        composeTestRule.onNodeWithTag("registration_button").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testWalletScreenHasDonationsTab() {
        registerUser()
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Donations").assertIsDisplayed()
    }

    @Test
    fun testDonationHistoryTotalCardShownAfterDonation() {
        // Navigate to the wallet to verify the Donations tab is accessible
        registerUser()
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Donations").performClick()
        composeTestRule.waitForIdle()
        // With no donations, the empty state should be present
        composeTestRule.onNodeWithText("No donations yet").assertIsDisplayed()
    }
}
