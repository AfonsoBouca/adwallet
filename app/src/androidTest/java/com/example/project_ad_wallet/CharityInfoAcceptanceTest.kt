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
 * Feature: Ver informações de charity
 *
 * Scenario: O utilizador vê a lista de instituições com detalhes e SDG
 *   Given o utilizador está logado
 *   When navega para o ecrã de Charities
 *   Then consegue ver as informações das Charities incluindo nome e SDG
 */
@RunWith(AndroidJUnit4::class)
class CharityInfoAcceptanceTest {

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
    fun charitiesScreen_displaysCharityInfoAndSDG() {
        registerUser()

        // Navigate to Charities
        // Wait, there is no direct bottom nav item for charities? 
        // Let's check how to navigate to charities.
        // It's in the Wallet screen via "Donate to Charities" button, or some other way.
        // Let's navigate to Wallet first.
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()

        // Click "Donations" tab
        composeTestRule.onNodeWithText("Donations").performClick()
        composeTestRule.waitForIdle()

        // Click "Donate to Charities" (the empty state button)
        composeTestRule.onNodeWithTag("donate_button_wallet_empty").performClick()
        composeTestRule.waitForIdle()

        // Verify that a charity is displayed with its SDG
        composeTestRule.onNodeWithText("Zero Hunger Foundation").assertIsDisplayed()
        composeTestRule.onNodeWithText("SDG 2 — Zero Hunger").assertIsDisplayed()
    }
}
