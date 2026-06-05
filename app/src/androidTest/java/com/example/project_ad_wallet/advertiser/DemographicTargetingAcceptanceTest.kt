package com.example.project_ad_wallet.advertiser

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.MainActivity
import com.example.project_ad_wallet.data.model.DemographicTarget
import com.example.project_ad_wallet.data.model.User
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
 * Feature: Demographic targeting de anúncios
 *
 * Scenario 1: Advertiser define parâmetros demográficos
 *   Given o advertiser está no dashboard
 *   When clica em "Targeting" e define idade 18-35 e género Male
 *   Then o anúncio é configurado com esses parâmetros
 *
 * Scenario 2: Utilizador fora do target não vê o anúncio
 *   Given um anúncio tem target de idade 18-25
 *   When um utilizador de 30 anos abre o feed
 *   Then esse anúncio não aparece
 *
 * Scenario 3: Utilizador dentro do target vê o anúncio
 *   Given um anúncio tem target de idade 18-35
 *   When um utilizador de 25 anos abre o feed
 *   Then esse anúncio aparece no feed
 */
@RunWith(AndroidJUnit4::class)
class DemographicTargetingAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TestEntryPoint {
        fun adStatsRepository(): com.example.project_ad_wallet.data.repository.AdStatsRepository
        fun adRepository(): com.example.project_ad_wallet.data.repository.AdRepository
        fun userRepository(): com.example.project_ad_wallet.data.repository.UserRepository
    }

    private fun getAdStatsRepository(): com.example.project_ad_wallet.data.repository.AdStatsRepository {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        ).adStatsRepository()
    }

    private fun getAdRepository(): com.example.project_ad_wallet.data.repository.AdRepository {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        ).adRepository()
    }

    private fun getUserRepository(): com.example.project_ad_wallet.data.repository.UserRepository {
        return EntryPointAccessors.fromApplication(
            composeTestRule.activity.applicationContext,
            TestEntryPoint::class.java
        ).userRepository()
    }

    @Before
    fun setup() {
        runBlocking {
            getAdStatsRepository().clearAdStats()
        }
        getAdRepository().reset()
        getUserRepository().reset()
    }

    @Test
    fun targetingDialog_setsCorrectParameters() {
        val statsRepo = getAdStatsRepository()
        runBlocking {
            statsRepo.recordView("1", "EcoBottle — Reusable Water Bottles")
        }

        composeTestRule.onNodeWithText("Email").performTextInput("advertiser@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("adv123")
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Targeting").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Male").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Confirmar").performClick()
        composeTestRule.waitForIdle()

        val ad = getAdRepository().getAdById("1")
        org.junit.Assert.assertNotNull(ad)
        org.junit.Assert.assertTrue(ad!!.demographicTarget.targetGenders.contains("Male"))
    }

    @Test
    fun userOutsideTarget_doesNotSeeAd() {
        getAdRepository().updateDemographicTarget(
            "2",
            DemographicTarget(minAge = 18, maxAge = 25)
        )

        val userRepo = getUserRepository()
        val existingUser = userRepo.getUser("user@adwallet.com")
        if (existingUser != null) {
            userRepo.saveUser(existingUser.copy(age = 30))
        } else {
            userRepo.saveUser(User(email = "user@adwallet.com", name = "Normal User", age = 30))
        }

        composeTestRule.onNodeWithText("Email").performTextInput("user@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("user123")
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("LearnFast — Online Courses").assertDoesNotExist()
    }

    @Test
    fun userInsideTarget_seesAd() {
        getAdRepository().updateDemographicTarget(
            "2",
            DemographicTarget(minAge = 18, maxAge = 35)
        )

        val userRepo = getUserRepository()
        val existingUser = userRepo.getUser("user@adwallet.com")
        if (existingUser != null) {
            userRepo.saveUser(existingUser.copy(age = 25))
        } else {
            userRepo.saveUser(User(email = "user@adwallet.com", name = "Normal User", age = 25))
        }

        composeTestRule.onNodeWithText("Email").performTextInput("user@adwallet.com")
        composeTestRule.onNodeWithText("Password").performTextInput("user123")
        composeTestRule.onNodeWithText("Entrar").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("LearnFast — Online Courses").assertIsDisplayed()
    }
}
