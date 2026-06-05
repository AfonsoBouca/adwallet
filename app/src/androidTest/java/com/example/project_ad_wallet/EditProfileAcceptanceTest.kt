package com.example.project_ad_wallet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Feature: Editar perfil
 *
 * Scenario: Utilizador edita o perfil com sucesso
 *   Given o utilizador está na tab Settings (Profile)
 *   When clica em Edit Profile e altera os dados e grava
 *   Then os novos dados são mostrados no ecrã de Settings
 */
@RunWith(AndroidJUnit4::class)
class EditProfileAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun registerUser() {
        composeTestRule.onNodeWithTag("registration_full_name").performTextInput("Old Name")
        composeTestRule.onNodeWithTag("registration_age").performTextInput("20")
        composeTestRule.onNodeWithText("Select your gender").performClick()
        composeTestRule.onNodeWithText("Male").performClick()
        composeTestRule.onNodeWithTag("registration_button").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun editProfileWithValidData_updatesProfile() {
        registerUser()

        // Navigate to Settings/Profile
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.waitForIdle()

        // Click Edit Profile
        composeTestRule.onNodeWithText("Edit Profile").performClick()
        composeTestRule.waitForIdle()

        // Change Name
        composeTestRule.onNodeWithTag("edit_profile_name").performTextInput(" New Name")
        
        // Save
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        // Verify updated name
        composeTestRule.onNodeWithText("Old Name New Name").assertIsDisplayed()
    }
}
