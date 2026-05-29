package com.example.project_ad_wallet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteAccountAcceptanceTest {

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
    fun testDeleteAccountButtonIsVisible() {
        registerUser()

        // Navigate to Settings
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        // Scroll to and verify delete account button is visible
        composeTestRule.onNodeWithTag("delete_account_button").performScrollTo()
        composeTestRule.onNodeWithTag("delete_account_button").assertIsDisplayed()
    }

    @Test
    fun testDeleteAccountShowsConfirmationDialog() {
        registerUser()

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("delete_account_button").performScrollTo()
        composeTestRule.onNodeWithTag("delete_account_button").performClick()
        composeTestRule.waitForIdle()

        // Verify confirmation dialog appears
        composeTestRule.onNodeWithText("Delete Account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }

    @Test
    fun testDeleteAccountConfirmNavigatesToRegistration() {
        registerUser()

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("delete_account_button").performScrollTo()
        composeTestRule.onNodeWithTag("delete_account_button").performClick()
        composeTestRule.waitForIdle()

        // Confirm deletion
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.waitForIdle()

        // Should return to registration screen
        composeTestRule.onNodeWithText("Join AdWallet").assertIsDisplayed()
    }

    @Test
    fun testDeleteAccountCancelStaysOnSettings() {
        registerUser()

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("delete_account_button").performScrollTo()
        composeTestRule.onNodeWithTag("delete_account_button").performClick()
        composeTestRule.waitForIdle()

        // Cancel deletion
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.waitForIdle()

        // Should stay on settings screen
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }
}
