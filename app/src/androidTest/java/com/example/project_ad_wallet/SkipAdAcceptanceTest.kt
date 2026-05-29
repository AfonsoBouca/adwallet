package com.example.project_ad_wallet

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SkipAdAcceptanceTest {

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
    fun testSkipIrrelevantAdRemovesItFromFeed() {
        registerUser()

        // Verify the EcoBottle ad is displayed initially
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").assertIsDisplayed()

        // Click on it
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").performClick()
        composeTestRule.waitForIdle()

        // Verify we are on watch screen and Skip Not Relevant button is displayed
        composeTestRule.onNodeWithTag("skip_not_relevant_button").assertIsDisplayed()

        // Click on Skip Not Relevant button
        composeTestRule.onNodeWithTag("skip_not_relevant_button").performClick()
        composeTestRule.waitForIdle()

        // Verify we are back on the feed and EcoBottle ad is gone, but other ads (like LearnFast) are still visible
        composeTestRule.onNodeWithText("LearnFast — Online Courses").assertIsDisplayed()
        
        // Assert EcoBottle is no longer shown
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").assertDoesNotExist()
    }
}
