package com.example.project_ad_wallet.streak

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.AdWalletApp
import com.example.project_ad_wallet.MainActivity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DatabaseEntryPoint {
    fun database(): com.example.project_ad_wallet.data.local.AdWalletDatabase
}

@RunWith(AndroidJUnit4::class)
class StreakAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun getDatabase(): com.example.project_ad_wallet.data.local.AdWalletDatabase {
        return EntryPoints.get(
            ApplicationProvider.getApplicationContext(),
            DatabaseEntryPoint::class.java
        ).database()
    }

    @Before
    fun setup() {
        runBlocking {
            getDatabase().userProfileDao().upsertProfile(
                UserProfileEntity(
                    id = 1,
                    name = "User",
                    email = "user@example.com",
                    streakDays = 3,
                    lastActiveDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1),
                    walletBalance = 10.0,
                    role = "USER"
                )
            )
        }
    }

    @Test
    fun streakWidget_isVisibleInFeed() {
        composeTestRule.onNodeWithTag("streak_widget").assertIsDisplayed()
        composeTestRule.onNodeWithTag("streak_days_text").assertTextEquals("3 dias")
    }

    @Test
    fun completingAd_incrementsStreak() {
        // Scenario 2: Streak incrementa após completar anúncio
        // Given o utilizador tem streak de 3 dias (already set in setup)
        
        // Find an ad and click it
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").performClick()
        
        // Skip through the video (wait for skip button or just wait)
        composeTestRule.mainClock.advanceTimeBy(10000)
        composeTestRule.onNodeWithText("SKIP TO QUESTIONS").performClick()
        
        // Complete questionnaire
        composeTestRule.onAllNodesWithText("156").onFirst().performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onAllNodesWithText("Stainless Steel").onFirst().performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        
        // Dismiss feedback
        composeTestRule.onNodeWithTag("feedback_continue_button").performClick()
        
        // Check reward dialog (it shows automatically in AdWatchScreen for 2s)
        composeTestRule.onNodeWithTag("reward_text").assertIsDisplayed()
        composeTestRule.mainClock.advanceTimeBy(3000) // wait for it to dismiss
        
        // Back to feed, check streak
        composeTestRule.onNodeWithTag("streak_days_text").assertTextEquals("4 dias")
    }

    @Test
    fun streakBonus_isAppliedToEarnings() {
        // Scenario 3: Bónus aplicado ao saldo com streak ativa
        // Given o utilizador tem streak de 7 dias (bónus 20%)
        runBlocking {
            getDatabase().userProfileDao().upsertProfile(
                UserProfileEntity(
                    id = 1,
                    name = "User",
                    email = "user@example.com",
                    streakDays = 6, // will become 7
                    lastActiveDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1),
                    walletBalance = 10.0,
                    role = "USER"
                )
            )
        }
        
        // Watch ad that gives 0.10 (EcoBottle rewardAmount is 0.10)
        composeTestRule.onNodeWithText("EcoBottle — Reusable Water Bottles").performClick()
        composeTestRule.mainClock.advanceTimeBy(10000)
        composeTestRule.onNodeWithText("SKIP TO QUESTIONS").performClick()
        composeTestRule.onAllNodesWithText("156").onFirst().performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onAllNodesWithText("Stainless Steel").onFirst().performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithTag("feedback_continue_button").performClick()
        
        // Check if reward is $0.12 (0.10 + 20% bonus)
        composeTestRule.onNodeWithTag("reward_text").assertTextContains("$0.12")
    }

    @Test
    fun brokenStreak_showsNotification() {
        // Scenario 4: Streak quebrada mostra notificação
        // Given o utilizador não viu anúncios há 2 dias
        runBlocking {
            getDatabase().userProfileDao().upsertProfile(
                UserProfileEntity(
                    id = 1,
                    name = "User",
                    email = "user@example.com",
                    streakDays = 10,
                    lastActiveDate = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2),
                    walletBalance = 10.0,
                    role = "USER"
                )
            )
        }
        
        // Restart activity to trigger check
        // In composeTestRule, we might need to recreate the activity
        // Or just rely on the combine in ViewModel which should trigger
        
        composeTestRule.onNodeWithText("Streak Interrompida").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").performClick()
        composeTestRule.onNodeWithText("Streak Interrompida").assertDoesNotExist()
    }
}
