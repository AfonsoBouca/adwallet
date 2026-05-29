package com.example.project_ad_wallet

import androidx.compose.ui.test.assertIsOn
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
class PromotionsNotificationAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun promotionsTogglePersistsWhenEnabled() {
        // Complete registration to reach main screens
        composeTestRule.onNodeWithText("Join AdWallet").assertIsDisplayed()
        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("Test User")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("30")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Other").performClick()
        composeTestRule.onNodeWithTag("registration_button").performClick()

        // Navigate to Profile (Settings)
        composeTestRule.onNodeWithText("Profile").performClick()

        // Open Notification Preferences
        composeTestRule.onNodeWithText("Notification Preferences").performClick()

        // Toggle promotions on
        val promotionsToggle = composeTestRule.onNodeWithTag("toggle_promotions")
        promotionsToggle.performClick()
        promotionsToggle.assertIsOn()

        // Navigate away and back to ensure persistence in UI
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Notification Preferences").performClick()

        // Verify promotions toggle remains enabled
        composeTestRule.onNodeWithTag("toggle_promotions").assertIsOn()
    }
}
