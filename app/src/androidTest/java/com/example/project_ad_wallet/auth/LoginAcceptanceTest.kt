package com.example.project_ad_wallet.auth

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.project_ad_wallet.MainActivity
import org.junit.Rule
import org.junit.Test

class LoginAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun validUserLogin_navigatesToFeed() {
        // Given a app está no ecrã de login
        composeTestRule.onNodeWithText("AdWallet").assertIsDisplayed()

        // When introduz email "user@adwallet.com" e password "user123" e clica Entrar
        composeTestRule.onNodeWithText("Email").performTextInput("user@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("user123")
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Then é navegado para o feed de anúncios
        // (No AdFeedScreen, o TopBar tem o título "Home")
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
    }

    @Test
    fun invalidCredentials_showsErrorMessage() {
        // Given a app está no ecrã de login
        composeTestRule.onNodeWithText("AdWallet").assertIsDisplayed()

        // When introduz credenciais erradas e clica Entrar
        composeTestRule.onNodeWithText("Email").performTextInput("wrong@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("wrongpass")
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Then aparece mensagem de erro "Email ou password incorretos."
        composeTestRule.onNodeWithText("Email ou password incorretos.").assertIsDisplayed()
    }

    @Test
    fun adminLogin_navigatesToAdminDashboard() {
        // Given a app está no ecrã de login
        composeTestRule.onNodeWithText("AdWallet").assertIsDisplayed()

        // When introduz email "admin@adwallet.com" e password "admin123"
        composeTestRule.onNodeWithText("Email").performTextInput("admin@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("admin123")
        composeTestRule.onNodeWithText("Entrar").performClick()

        // Then é navegado para o Admin Dashboard
        composeTestRule.onNodeWithText("Admin Dashboard").assertIsDisplayed()
    }
}
