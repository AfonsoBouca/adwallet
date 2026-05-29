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
class EarningsHistoryAcceptanceTest {

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
    fun testEarningsHistoryShowsEmptyStateInitially() {
        registerUser()
        // Navigate to Wallet tab
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()
        // Navigate to Earnings tab
        composeTestRule.onNodeWithText("Earnings").performClick()
        composeTestRule.waitForIdle()
        // Should see empty state
        composeTestRule.onNodeWithText("No earnings yet. Watch an ad to earn!").assertIsDisplayed()
    }

    @Test
    fun testWalletScreenShowsCurrentBalance() {
        registerUser()
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()
        // Balance tab should show $0.00
        composeTestRule.onNodeWithText("Current Balance").assertIsDisplayed()
    }
}
