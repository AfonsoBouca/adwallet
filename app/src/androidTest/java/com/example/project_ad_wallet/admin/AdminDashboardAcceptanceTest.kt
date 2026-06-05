package com.example.project_ad_wallet.admin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Feature: Monitorização de atividade — Admin
 *
 * Scenario 1: Admin vê atividade recente
 *   Given o utilizador está autenticado como ADMIN
 *   When abre o Admin Dashboard
 *   Then vê a lista de atividade recente dos utilizadores
 *
 * Scenario 2: Admin vê atividade suspeita separada
 *   Given existem atividades flagged no sistema
 *   When o admin clica na tab "Suspeitos"
 *   Then vê apenas as atividades marcadas como suspeitas
 *
 * Scenario 3: Admin faz flag de atividade suspeita
 *   Given o admin está na tab de atividade recente
 *   When clica no ícone de flag numa atividade
 *   Then a atividade move-se para a tab de Suspeitos
 */
@RunWith(AndroidJUnit4::class)
class AdminDashboardAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TestEntryPoint {
        fun userActivityRepository(): com.example.project_ad_wallet.data.repository.UserActivityRepository
        fun adWalletDatabase(): com.example.project_ad_wallet.data.local.AdWalletDatabase
    }

    private fun getUserActivityRepository(): com.example.project_ad_wallet.data.repository.UserActivityRepository {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        ).userActivityRepository()
    }

    private fun getAdWalletDatabase(): com.example.project_ad_wallet.data.local.AdWalletDatabase {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        ).adWalletDatabase()
    }

    @Before
    fun setup() {
        getAdWalletDatabase().clearAllTables()
    }

    private fun loginAsAdmin() {
        composeTestRule.onNodeWithText("Email").performTextInput("admin@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("admin123")
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun dashboard_showsRecentActivity() {
        val repo = getUserActivityRepository()
        runBlocking {
            repo.logActivity("John Doe", "AD_WATCHED", "Watched EcoBottle ad", userId = 1)
            repo.logActivity("Jane Doe", "DONATION", "Donated $2.00", userId = 2)
        }

        loginAsAdmin()

        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("AD_WATCHED").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jane Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("DONATION").assertIsDisplayed()
    }

    @Test
    fun suspiciousTab_showsFlaggedActivity() {
        val repo = getUserActivityRepository()
        runBlocking {
            repo.logActivity(
                "Spammer", "AD_WATCHED", "Watched 25 ads in 1 hour",
                isFlagged = true, flagReason = "Too many ads per hour", userId = 1
            )
            repo.logActivity("Good User", "DONATION", "Donated $1.00", isFlagged = false, userId = 2)
        }

        loginAsAdmin()

        composeTestRule.onNodeWithText("Suspeitos (1)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Spammer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Good User").assertDoesNotExist()
    }

    @Test
    fun flagActivity_movesToSuspiciousTab() {
        val repo = getUserActivityRepository()
        runBlocking {
            repo.logActivity("Naughty User", "AD_WATCHED", "Watched ad too fast", isFlagged = false, userId = 1)
        }

        loginAsAdmin()

        composeTestRule.onNodeWithText("Naughty User").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Flag").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Suspeitos (1)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Naughty User").assertIsDisplayed()
        composeTestRule.onNodeWithText("⚠️ Flagged manually").assertIsDisplayed()
    }
}
