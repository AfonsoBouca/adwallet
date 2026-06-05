package com.example.project_ad_wallet

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Feature: Histórico de Ganhos
 *
 * Scenario 1: Utilizador sem ganhos vê empty state
 *   Given o utilizador não tem ganhos registados
 *   When navega para o histórico de ganhos
 *   Then vê a mensagem "Ainda não ganhaste saldo"
 *
 * Scenario 2: Utilizador com ganhos vê a lista
 *   Given o utilizador tem ganhos registados
 *   When navega para o histórico de ganhos
 *   Then vê a lista com os títulos dos anúncios e valores ganhos
 *
 * Scenario 3: Total ganho é apresentado corretamente
 *   Given o utilizador tem múltiplos ganhos
 *   When abre o histórico de ganhos
 *   Then vê o total acumulado no topo do ecrã
 *
 * Scenario 4: Ganho com bónus de streak mostra indicador
 *   Given o utilizador tem um ganho com bónus de streak
 *   When abre o histórico de ganhos
 *   Then vê o indicador de bónus 🔥 no item correspondente
 */
@RunWith(AndroidJUnit4::class)
class EarningsHistoryAcceptanceTest {

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

    @Before
    fun setup() {
        val app = ApplicationProvider.getApplicationContext<AdWalletApp>()
        val database = app.database
        runBlocking {
            database.clearAllTables()
            database.userProfileDao().upsertProfile(
                UserProfileEntity(
                    id = 1,
                    name = "Test User",
                    email = "user@example.com",
                    streakDays = 0,
                    lastActiveDate = System.currentTimeMillis(),
                    walletBalance = 0.0,
                    role = "USER"
                )
            )
        }
    }

    private fun navigateToEarningsHistory() {
        // Go to Wallet tab
        composeTestRule.onNodeWithText("Wallet").performClick()
        composeTestRule.waitForIdle()

        // Go to Earnings tab
        composeTestRule.onNodeWithText("Earnings").performClick()
        composeTestRule.waitForIdle()

        // Click on "Ver Histórico" (see_full_history_button)
        composeTestRule.onNodeWithTag("see_full_history_button").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun noEarnings_showsEmptyState() {
        registerUser()
        navigateToEarningsHistory()
        composeTestRule.onNodeWithText("Ainda não ganhaste saldo").assertIsDisplayed()
    }

    @Test
    fun withEarnings_showsList() {
        val app = ApplicationProvider.getApplicationContext<AdWalletApp>()
        runBlocking {
            app.database.earningsDao().insertEarning(
                EarningEntity(
                    id = 1,
                    adTitle = "Test Ad 1",
                    adId = "ad1",
                    amount = 0.50,
                    baseAmount = 0.50,
                    streakBonus = 0.0,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        registerUser()
        navigateToEarningsHistory()

        composeTestRule.onNodeWithText("Test Ad 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("+€0.50").assertIsDisplayed()
    }

    @Test
    fun totalEarned_isDisplayedCorrectly() {
        val app = ApplicationProvider.getApplicationContext<AdWalletApp>()
        runBlocking {
            app.database.earningsDao().insertEarning(
                EarningEntity(
                    id = 1,
                    adTitle = "Test Ad 1",
                    adId = "ad1",
                    amount = 0.50,
                    baseAmount = 0.50,
                    streakBonus = 0.0,
                    timestamp = System.currentTimeMillis() - 1000
                )
            )
            app.database.earningsDao().insertEarning(
                EarningEntity(
                    id = 2,
                    adTitle = "Test Ad 2",
                    adId = "ad2",
                    amount = 1.50,
                    baseAmount = 1.50,
                    streakBonus = 0.0,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        registerUser()
        navigateToEarningsHistory()

        composeTestRule.onNodeWithText("€2.00").assertIsDisplayed()
    }

    @Test
    fun earningWithStreakBonus_showsBonusIndicator() {
        val app = ApplicationProvider.getApplicationContext<AdWalletApp>()
        runBlocking {
            app.database.earningsDao().insertEarning(
                EarningEntity(
                    id = 1,
                    adTitle = "Streak Ad",
                    adId = "ad1",
                    amount = 1.20,
                    baseAmount = 1.00,
                    streakBonus = 0.20,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        registerUser()
        navigateToEarningsHistory()

        composeTestRule.onNodeWithText("Streak Ad").assertIsDisplayed()
        composeTestRule.onNodeWithText("🔥 Bónus de streak: +20%").assertIsDisplayed()
        composeTestRule.onNodeWithText("+€1.20").assertIsDisplayed()
    }
}
