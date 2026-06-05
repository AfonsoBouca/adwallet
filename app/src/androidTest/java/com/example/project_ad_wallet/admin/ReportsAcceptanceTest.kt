package com.example.project_ad_wallet.admin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.MainActivity
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
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
 * Feature: Relatórios de plataforma — Admin
 *
 * Scenario 1: Admin gera relatório da plataforma
 *   Given o admin está no Admin Dashboard
 *   When navega para a tab Relatórios e clica em Gerar Relatório
 *   Then vê um card com as métricas da plataforma
 *
 * Scenario 2: Relatório mostra total de anúncios vistos
 *   Given existem anúncios vistos registados
 *   When o admin gera o relatório
 *   Then vê o número correto de anúncios vistos
 *
 * Scenario 3: Relatório mostra charity mais apoiada
 *   Given existem doações para múltiplas charities
 *   When o admin gera o relatório
 *   Then vê a charity com maior valor doado destacada
 */
@RunWith(AndroidJUnit4::class)
class ReportsAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TestEntryPoint {
        fun adWalletDatabase(): com.example.project_ad_wallet.data.local.AdWalletDatabase
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

    private fun navigateToReportsTabAndGenerate() {
        composeTestRule.onNodeWithText("Relatórios").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Gerar Relatório").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun generateReport_showsMetricsCard() {
        loginAsAdmin()
        navigateToReportsTabAndGenerate()

        composeTestRule.onNodeWithText("Relatório gerado em", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("📺 Anúncios vistos").assertIsDisplayed()
        composeTestRule.onNodeWithText("💸 Total doado").assertIsDisplayed()
    }

    @Test
    fun report_showsCorrectAdsWatched() {
        val db = getAdWalletDatabase()
        runBlocking {
            db.earningsDao().insertEarning(
                EarningEntity(adTitle = "Eco Ad 1", adId = "ad_1", amount = 0.50)
            )
            db.earningsDao().insertEarning(
                EarningEntity(adTitle = "Eco Ad 2", adId = "ad_2", amount = 1.20)
            )
        }

        loginAsAdmin()
        navigateToReportsTabAndGenerate()

        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("€1.70").assertIsDisplayed()
    }

    @Test
    fun report_showsTopCharity() {
        val db = getAdWalletDatabase()
        runBlocking {
            db.donationsDao().insertDonation(
                DonationEntity(charityName = "Red Cross", charityId = "c1", amount = 10.0)
            )
            db.donationsDao().insertDonation(
                DonationEntity(charityName = "Red Cross", charityId = "c1", amount = 5.0)
            )
            db.donationsDao().insertDonation(
                DonationEntity(charityName = "UNICEF", charityId = "c2", amount = 8.0)
            )
        }

        loginAsAdmin()
        navigateToReportsTabAndGenerate()

        composeTestRule.onNodeWithText("Red Cross").assertIsDisplayed()
        composeTestRule.onNodeWithText("€15.00").assertIsDisplayed()
    }
}
