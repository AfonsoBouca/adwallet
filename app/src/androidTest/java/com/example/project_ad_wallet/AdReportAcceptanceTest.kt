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
 * Feature: Reportar anúncio
 *
 * Scenario: Utilizador submete report de anúncio com sucesso
 *   Given o utilizador está no ecrã de um anúncio
 *   When clica em reportar, preenche o motivo e confirma
 *   Then recebe uma mensagem de sucesso e é devolvido ao feed
 */
@RunWith(AndroidJUnit4::class)
class AdReportAcceptanceTest {

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
    fun afterReportingAd_userIsReturnedToFeed() {
        registerUser()

        // Open an ad
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").performClick()
        composeTestRule.waitForIdle()

        // Open report dialog
        composeTestRule.onNodeWithTag("report_ad_button").performClick()
        composeTestRule.waitForIdle()

        // Select a reason and submit
        composeTestRule.onNodeWithTag("reason_Misleading content").performClick()
        composeTestRule.onNodeWithTag("submit_report_button").performClick()
        composeTestRule.waitForIdle()

        // Confirm report
        composeTestRule.onNodeWithTag("report_ok_button").performClick()
        composeTestRule.waitForIdle()

        // Verify we are back at the feed
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        
        // Verify the reported ad is gone (since it was marked as skipped)
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").assertDoesNotExist()
    }
}
