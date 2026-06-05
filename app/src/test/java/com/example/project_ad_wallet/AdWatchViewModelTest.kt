package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.model.Question
import com.example.project_ad_wallet.data.model.QuestionOption
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.fakes.FakeAdsWatchedDao
import com.example.project_ad_wallet.fakes.FakeEarningsDao
import com.example.project_ad_wallet.fakes.FakeUserProfileDao
import com.example.project_ad_wallet.ui.adWatch.AdWatchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdWatchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var adRepo: AdRepository
    private lateinit var walletRepo: WalletRepository
    private lateinit var viewModel: AdWatchViewModel

    private val fakeAd = Ad(
        id = "test-ad",
        title = "TestAd",
        description = "A test ad",
        thumbnailResId = 0,
        videoResId = 0,
        rewardAmount = 0.20,
        advertiserName = "Test Co",
        category = "Test",
        status = AdStatus.APPROVED,
    )

    private val fakeQuestions = listOf(
        Question(
            text = "Q1",
            options = listOf(
                QuestionOption("A", false),
                QuestionOption("B", true),
            ),
        ),
        Question(
            text = "Q2",
            options = listOf(
                QuestionOption("X", true),
                QuestionOption("Y", false),
            ),
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val adsWatchedDao = FakeAdsWatchedDao()
        adRepo = AdRepository(adsWatchedDao, kotlinx.coroutines.CoroutineScope(testDispatcher))
        adRepo.addAd(fakeAd)
        adRepo.addQuestionnaire("test-ad", fakeQuestions)
        
        val earningsDao = FakeEarningsDao()
        val userProfileDao = FakeUserProfileDao()
        walletRepo = WalletRepository(earningsDao, userProfileDao, kotlinx.coroutines.CoroutineScope(testDispatcher))
        
        viewModel = AdWatchViewModel(adRepo, walletRepo)
        viewModel.loadAd("test-ad")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_rewardIsAddedToWalletAfterCompletingQuestionnaire() = runTest {
        viewModel.proceedToQuestionnaire()
        viewModel.completeQuestionnaire()

        assertEquals(0.20, walletRepo.balance.value, 0.001)
        assertEquals(1, walletRepo.earnings.value.size)
    }

    @Test
    fun test_skipIsDisabledUntil80Percent() = runTest {
        viewModel.updateProgress(0.5f)
        assertFalse(viewModel.uiState.value.isSkipEnabled)

        viewModel.updateProgress(0.81f)
        assertTrue(viewModel.uiState.value.isSkipEnabled)
    }

    @Test
    fun test_rewardNotGrantedIfQuestionnaireNotCompleted() = runTest {
        assertEquals(0.0, walletRepo.balance.value, 0.001)
        assertEquals(0, walletRepo.earnings.value.size)
    }

    @Test
    fun test_earningEntryHasCorrectAdTitleAndAmount() = runTest {
        viewModel.completeQuestionnaire()

        val earning = walletRepo.earnings.value.firstOrNull()
        assertNotNull(earning)
        assertEquals("TestAd", earning!!.adTitle)
        assertEquals(0.20, earning.amount, 0.001)
    }

    @Test
    fun test_adIsMarkedWatchedAfterCompletion() = runTest {
        viewModel.completeQuestionnaire()
        assertTrue(adRepo.watchedAdIds.value.contains("test-ad"))
    }

    @Test
    fun test_selectCorrectAnswerSetsIsAnswerCorrectTrue() = runTest {
        viewModel.proceedToQuestionnaire()
        // Q1 correct answer is index 1 ("B")
        viewModel.selectAnswer(1)
        assertTrue(viewModel.uiState.value.isAnswerCorrect == true)
    }

    @Test
    fun test_selectWrongAnswerSetsIsAnswerCorrectFalse() = runTest {
        viewModel.proceedToQuestionnaire()
        // Q1 wrong answer is index 0 ("A")
        viewModel.selectAnswer(0)
        assertTrue(viewModel.uiState.value.isAnswerCorrect == false)
    }

    @Test
    fun test_advanceQuestionMovesToNext() = runTest {
        viewModel.proceedToQuestionnaire()
        assertEquals(0, viewModel.uiState.value.currentQuestionIndex)
        viewModel.selectAnswer(0)
        viewModel.advanceQuestion()
        assertEquals(1, viewModel.uiState.value.currentQuestionIndex)
        assertNull(viewModel.uiState.value.selectedAnswerIndex)
    }

    @Test
    fun test_skipAdMarksAdAsSkipped() = runTest {
        assertFalse(adRepo.skippedAdIds.value.contains("test-ad"))
        viewModel.skipAd()
        assertTrue(adRepo.skippedAdIds.value.contains("test-ad"))
    }
}
