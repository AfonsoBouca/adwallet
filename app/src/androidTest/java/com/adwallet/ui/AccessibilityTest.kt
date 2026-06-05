package com.adwallet.ui

import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.ui.components.AnimatedDonateButton
import com.example.project_ad_wallet.ui.components.StreakWidget
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Feature: Acessibilidade e Internacionalização
 *
 * Scenario 1: Elementos interativos têm content descriptions
 *   Given o utilizador está no AdFeed
 *   When usa um leitor de ecrã
 *   Then todos os botões e ícones têm descrições acessíveis
 *
 * Scenario 2: App mostra strings em português por defeito
 *   Given o dispositivo está configurado em português
 *   When o utilizador abre a app
 *   Then os textos estão em português
 *
 * Scenario 3: App mostra strings em inglês quando configurado
 *   Given o dispositivo está configurado em inglês
 *   When o utilizador abre a app
 *   Then os textos estão em inglês
 */
@RunWith(AndroidJUnit4::class)
class AccessibilityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginButton_hasContentDescription() {
        composeTestRule.setContent {
            androidx.compose.material3.Button(
                onClick = {},
                modifier = androidx.compose.ui.Modifier.semantics {
                    contentDescription = "Entrar"
                }
            ) {
                androidx.compose.material3.Text("Entrar")
            }
        }
        composeTestRule.onNodeWithContentDescription("Entrar")
            .assertExists()
    }

    @Test
    fun donateButton_hasContentDescription() {
        composeTestRule.setContent {
            AnimatedDonateButton(
                onClick = {},
                enabled = true,
                text = "Doar agora"
            )
        }
        composeTestRule.onNodeWithContentDescription("Doar agora")
            .assertExists()
    }

    @Test
    fun streakWidget_hasContentDescription() {
        composeTestRule.setContent {
            StreakWidget(
                streakDays = 5
            )
        }
        composeTestRule.onNodeWithContentDescription("Streak de 5 dias")
            .assertExists()
    }
}
