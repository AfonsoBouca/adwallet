package com.example.project_ad_wallet.advertiser

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
 * Feature: Estatísticas de anúncios para Advertiser
 *
 * Scenario 1: Advertiser vê dashboard com estatísticas
 *   Given o utilizador está autenticado como ADVERTISER
 *   When abre o Advertiser Dashboard
 *   Then vê os cards de Total Views, Completions e Completion Rate
 *
 * Scenario 2: Dashboard sem dados mostra empty state
 *   Given não há anúncios com visualizações
 *   When o advertiser abre o dashboard
 *   Then vê a mensagem "Sem dados ainda"
 *
 * Scenario 3: Lista de anúncios mostra estatísticas individuais
 *   Given há anúncios com visualizações registadas
 *   When o advertiser abre o dashboard
 *   Then vê cada anúncio com views, completions e rating
 */
@RunWith(AndroidJUnit4::class)
class AdvertiserDashboardAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TestEntryPoint {
        fun adStatsRepository(): com.example.project_ad_wallet.data.repository.AdStatsRepository
    }

    private fun getAdStatsRepository(): com.example.project_ad_wallet.data.repository.AdStatsRepository {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        )
    }

    @Before
    fun setup() {
        runBlocking {
            getAdStatsRepository().clearAdStats()
        }
    }

    private fun loginAsAdvertiser() {
        composeTestRule.onNodeWithText("Email").performTextInput("advertiser@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("adv123")
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun dashboard_showsSummaryCards() {
        loginAsAdvertiser()

        composeTestRule.onNodeWithText("Advertiser Dashboard").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total Views").assertIsDisplayed()
        composeTestRule.onNodeWithText("Completions").assertIsDisplayed()
        composeTestRule.onNodeWithText("Completion Rate").assertIsDisplayed()
    }

    @Test
    fun emptyDashboard_showsEmptyState() {
        loginAsAdvertiser()

        composeTestRule.onNodeWithText("Sem dados ainda").assertIsDisplayed()
        composeTestRule.onNodeWithText("As estatísticas aparecem quando os utilizadores virem os teus anúncios.").assertIsDisplayed()
    }

    @Test
    fun adList_showsIndividualStats() {
        val repo = getAdStatsRepository()
        runBlocking {
            repo.recordView("test-ad-1", "Ad Acceptance Test 1")
            repo.recordCompletion("test-ad-1", "Ad Acceptance Test 1", 1.5)
            repo.recordRating("test-ad-1", "Ad Acceptance Test 1", 4.0)
        }

        loginAsAdvertiser()

        composeTestRule.onNodeWithText("Ad Acceptance Test 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("👁 1 views").assertIsDisplayed()
        composeTestRule.onNodeWithText("✅ 1 completions").assertIsDisplayed()
        composeTestRule.onNodeWithText("⭐ 4.0").assertIsDisplayed()
    }
}
