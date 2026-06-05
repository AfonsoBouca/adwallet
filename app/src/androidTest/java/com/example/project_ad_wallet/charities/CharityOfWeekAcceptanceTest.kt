package com.example.project_ad_wallet.charities

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.MainActivity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
class CharityOfWeekAcceptanceTest {

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
                    name = "Test User",
                    email = "user@example.com",
                    streakDays = 0,
                    lastActiveDate = System.currentTimeMillis(),
                    walletBalance = 10.0,
                    role = "USER"
                )
            )
            getDatabase().charityOfWeekDao().upsertCharityOfWeek(
                CharityOfWeekEntity(
                    id = 1,
                    charityId = "unicef",
                    charityName = "UNICEF",
                    charityDescription = "Protege os direitos das crianças em todo o mundo.",
                    sdgNumber = 3,
                    sdgDescription = "Boa Saúde e Bem-Estar",
                    logoRes = "ic_unicef",
                    weekStartTimestamp = System.currentTimeMillis(),
                    totalDonatedThisWeek = 0.0
                )
            )
        }
    }

    @Test
    fun charityOfWeekCard_isVisibleInFeed() {
        // Scenario 1: Utilizador vê a Charity da Semana no feed
        composeTestRule.onNodeWithText("✨ Charity da Semana").assertIsDisplayed()
        composeTestRule.onNodeWithText("UNICEF").assertIsDisplayed()
        composeTestRule.onNodeWithText("Protege os direitos das crianças em todo o mundo.").assertIsDisplayed()
        composeTestRule.onNodeWithText("ODS 3").assertIsDisplayed()
    }

    @Test
    fun donateButton_navigatesToDonationScreen() {
        // Scenario 2: Utilizador doa para a Charity da Semana
        composeTestRule.onNodeWithText("Doar agora").performClick()
        composeTestRule.waitForIdle()

        // Then é navegado para o ecrã de doação com a charity pré-selecionada
        composeTestRule.onNodeWithText("Donate to UNICEF").assertIsDisplayed()
    }

    @Test
    fun totalDonated_updatesAfterDonation() {
        // Scenario 3: Total doado esta semana é atualizado após doação
        composeTestRule.onNodeWithText("Doar agora").performClick()
        composeTestRule.waitForIdle()

        // Select $5 quick amount
        composeTestRule.onNodeWithText("$5").performClick()
        composeTestRule.waitForIdle()

        // Click "Confirm Donation"
        composeTestRule.onNodeWithText("Confirm Donation").performClick()
        composeTestRule.waitForIdle()

        // Verify navigation to success screen and balance deduction
        composeTestRule.onNodeWithText("Donation Successful!").assertIsDisplayed()
        composeTestRule.onNodeWithText("DONE").performClick()
        composeTestRule.waitForIdle()

        // Now we are back on CharitiesScreen, and the weekly card should display the updated total donation of €5.00
        composeTestRule.onNodeWithText("Doado esta semana:", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Doado esta semana:", substring = true).assertTextContains("5")
    }
}
