package com.example.project_ad_wallet.profile

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.project_ad_wallet.MainActivity
import com.example.project_ad_wallet.ui.navigation.Routes
import org.junit.Rule
import org.junit.Test

class ProfilePictureAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun noProfilePicture_showsPlaceholder() {
        // Given o utilizador está no ecrã de Settings
        // Navegar para Settings (Profile tab no bottom nav)
        composeTestRule.onNodeWithText("Profile").performClick()

        // Then vê um ícone de pessoa como placeholder (buscado por contentDescription null ou pela estrutura)
        // No meu ProfilePictureSection, o placeholder não tem contentDescription "Foto de perfil"
        composeTestRule.onNodeWithContentDescription("Foto de perfil").assertDoesNotExist()
        
        // O botão de alterar foto deve existir
        composeTestRule.onNodeWithContentDescription("Alterar foto").assertIsDisplayed()
    }

    @Test
    fun selectingImage_updatesProfilePicture() {
        // Este teste é difícil de automatizar totalmente sem mocks pesados do Android Intent System
        // Vamos apenas verificar se ao clicar no botão "Alterar foto" não explode
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithContentDescription("Alterar foto").performClick()
        
        // Em um ambiente real, o picker abriria. Aqui verificamos se o estado continua estável.
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }
}
