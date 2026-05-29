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
class ResetPasswordAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun resetPassword_allowsUserToLoginWithNewPassword() {
        composeTestRule.onNodeWithText("Join AdWallet").assertIsDisplayed()
        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("Test User")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("30")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Other").performClick()
        composeTestRule.onNodeWithTag("registration_email").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("registration_password").performTextInput("oldpass")
        composeTestRule.onNodeWithTag("registration_button").performClick()

        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithTag("logout_button").performClick()

        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
        composeTestRule.onNodeWithTag("forgot_password_button").performClick()

        composeTestRule.onNodeWithTag("reset_email").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("reset_password").performTextInput("newpass")
        composeTestRule.onNodeWithTag("reset_confirm_password").performTextInput("newpass")
        composeTestRule.onNodeWithTag("reset_password_button").performClick()

        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_email").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("login_password").performTextInput("newpass")
        composeTestRule.onNodeWithTag("login_button").performClick()

        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").assertIsDisplayed()
    }
}
