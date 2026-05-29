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
class UserAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testRegistrationMandatoryFields() {
        composeTestRule.onNodeWithText("Join AdWallet").assertIsDisplayed()

        composeTestRule.onNodeWithTag("registration_button").performClick()
        composeTestRule.onNodeWithText("Join AdWallet").assertIsDisplayed()

        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("John Doe")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("25")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Male").performClick()

        composeTestRule.onNodeWithTag("registration_button").performClick()

        composeTestRule.onNodeWithText("Nike Running Shoes").assertIsDisplayed()
    }
}
