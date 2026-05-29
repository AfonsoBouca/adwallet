package com.example.project_ad_wallet

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.data.model.QuestionnaireResponse
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.ui.advertiser.AdvertiserResponsesScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdvertiserResponsesAcceptanceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun advertiser_sees_questionnaire_responses() {
        val repo = AdRepository.instance
        repo.reset()

        val sample = QuestionnaireResponse(adId = "1", answers = listOf(1, 0))
        repo.addQuestionnaireResponse(sample)

        composeTestRule.setContent {
            AdvertiserResponsesScreen(adRepository = repo)
        }

        val adTitle = repo.getAdById(sample.adId)?.title ?: "Unknown Ad"
        composeTestRule.onNodeWithText(adTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText("Answers: 1, 0").assertIsDisplayed()
    }
}
