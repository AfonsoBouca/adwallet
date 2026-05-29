package com.example.project_ad_wallet

import androidx.compose.ui.test.assertIsDisplayed
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
class AdvertiserUploadAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun registerAndNavigateToUpload() {
        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("Test User")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("25")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Male").performClick()
        composeTestRule.onNodeWithTag("registration_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Advertiser? Upload Ad").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testAdvertiserCanSeeUploadForm() {
        registerAndNavigateToUpload()
        composeTestRule.onNodeWithText("Upload Your Advertisement").assertIsDisplayed()
    }

    @Test
    fun testAdvertiserCannotSubmitWithEmptyTitle() {
        registerAndNavigateToUpload()
        // Leave title empty, fill other fields
        composeTestRule.onNodeWithTag("upload_company").performTextInput("TestCo")
        composeTestRule.onNodeWithTag("upload_description").performTextInput("A great product.")
        composeTestRule.onNodeWithTag("upload_reward").performTextInput("0.25")
        composeTestRule.onNodeWithTag("submit_ad_button").performClick()
        composeTestRule.waitForIdle()
        // Should still be on upload screen (error shown)
        composeTestRule.onNodeWithText("Upload Your Advertisement").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title is required").assertIsDisplayed()
    }
}
