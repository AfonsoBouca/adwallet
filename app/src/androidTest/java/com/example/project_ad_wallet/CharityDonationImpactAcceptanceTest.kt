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

@RunWith(AndroidJUnit4::class)
class CharityDonationImpactAcceptanceTest {

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

    private fun addFundsAndNavigateToCharities() {
        // Go to settings/profile
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        // Click "+$50" under Developer Options to add funds
        composeTestRule.onNodeWithText("+$50").performClick()
        composeTestRule.waitForIdle()

        // Go to Wallet screen
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()

        // Click on Donations tab
        composeTestRule.onNodeWithText("Donations").performClick()
        composeTestRule.waitForIdle()

        // Click on Donate to Charities button
        composeTestRule.onNodeWithTag("donate_button_wallet_empty").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testCharityInitialDonationAmountAndImpactUpdatesAfterDonation() {
        registerUser()
        addFundsAndNavigateToCharities()

        // We are on Charities screen. Assert the Charities header is displayed.
        composeTestRule.onNodeWithText("Charities").assertIsDisplayed()

        // Verify initial donated amount for Zero Hunger Foundation is $0.00
        composeTestRule.onNodeWithTag("charity_total_donated_zero-hunger-foundation").assertIsDisplayed()
        androidx.compose.ui.test.assertTextEquals(composeTestRule.onNodeWithTag("charity_total_donated_zero-hunger-foundation"), "Total Donated: $0.00")

        // Click on Zero Hunger Foundation to open the donation dialog
        composeTestRule.onNodeWithText("Zero Hunger Foundation").performClick()
        composeTestRule.waitForIdle()

        // Enter donation amount of $5.00
        composeTestRule.onNodeWithText("Confirm Donation").assertIsDisplayed()
        composeTestRule.onNodeWithText("$5").performClick()

        // Click Confirm Donation
        composeTestRule.onNodeWithText("Confirm Donation").performClick()
        composeTestRule.waitForIdle()

        // Confirm we get redirected to Donation Confirmation Screen and click back to charities
        composeTestRule.onNodeWithText("Donation Successful!").assertIsDisplayed()
        composeTestRule.onNodeWithText("BACK TO CHARITIES").performClick()
        composeTestRule.waitForIdle()

        // Assert we are back on Charities screen and the total donated has updated to $5.00!
        composeTestRule.onNodeWithTag("charity_total_donated_zero-hunger-foundation").assertIsDisplayed()
        androidx.compose.ui.test.assertTextEquals(composeTestRule.onNodeWithTag("charity_total_donated_zero-hunger-foundation"), "Total Donated: $5.00")
    }
}
