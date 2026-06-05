package com.example.project_ad_wallet.ui.adWatch

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.model.Question
import com.example.project_ad_wallet.data.model.QuestionResult
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.AdStatsRepository
import com.example.project_ad_wallet.data.repository.UserActivityRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AdWatchUiState(
    val ad: Ad? = null,
    val isSkipEnabled: Boolean = false,
    val watchProgress: Float = 0f,
    val showQuestionnaire: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val questions: List<Question> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean? = null,
    val rewardGranted: Boolean = false,
    val rewardAmount: Double = 0.0,
    val isComplete: Boolean = false,
    val showFeedback: Boolean = false,
    val questionResults: List<QuestionResult> = emptyList(),
    val correctCount: Int = 0,
    val totalQuestions: Int = 0,
)

@HiltViewModel
class AdWatchViewModel @Inject constructor(
    private val adRepository: AdRepository,
    private val walletRepository: WalletRepository,
    private val adStatsRepository: AdStatsRepository,
    private val userRepository: UserRepository,
    private val userActivityRepository: UserActivityRepository,
    private val userActivityDao: UserActivityDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdWatchUiState())
    val uiState: StateFlow<AdWatchUiState> = _uiState.asStateFlow()

    fun loadAd(adId: String) {
        val ad = adRepository.getAdById(adId) ?: return
        val questions = adRepository.getQuestionsForAd(adId)
        _uiState.update {
            it.copy(
                ad = ad,
                questions = questions,
                rewardAmount = ad.rewardAmount,
            )
        }
        viewModelScope.launch {
            adStatsRepository.recordView(ad.id, ad.title)
        }
    }

    fun updateProgress(progress: Float) {
        _uiState.update {
            it.copy(
                watchProgress = progress,
                isSkipEnabled = progress >= 0.8f,
            )
        }
    }

    fun proceedToQuestionnaire() {
        _uiState.update { it.copy(showQuestionnaire = true, currentQuestionIndex = 0) }
    }

    fun selectAnswer(answerIndex: Int) {
        val state = _uiState.value
        val question = state.questions.getOrNull(state.currentQuestionIndex) ?: return
        val isCorrect = question.options.getOrNull(answerIndex)?.isCorrect == true
        val selectedText = question.options.getOrNull(answerIndex)?.text ?: ""
        val correctText = question.options.firstOrNull { it.isCorrect }?.text ?: ""
        val result = QuestionResult(
            questionText = question.text,
            selectedAnswer = selectedText,
            correctAnswer = correctText,
            isCorrect = isCorrect,
        )
        _uiState.update {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = isCorrect,
                questionResults = it.questionResults + result,
            )
        }
    }

    fun advanceQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentQuestionIndex + 1
        if (nextIndex >= state.questions.size) {
            showFeedbackScreen()
        } else {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedAnswerIndex = null,
                    isAnswerCorrect = null,
                )
            }
        }
    }

    private fun showFeedbackScreen() {
        val state = _uiState.value
        val correct = state.questionResults.count { it.isCorrect }
        _uiState.update {
            it.copy(
                showFeedback = true,
                showQuestionnaire = false,
                correctCount = correct,
                totalQuestions = state.questions.size,
            )
        }
    }

    fun dismissFeedback() {
        _uiState.update { it.copy(showFeedback = false) }
        completeQuestionnaire()
    }

    fun completeQuestionnaire() {
        val state = _uiState.value
        val ad = state.ad ?: return

        viewModelScope.launch {
            val totalAmount = walletRepository.completeAdAndUpdateStreak(
                adTitle = ad.title,
                adId = ad.id,
                baseAmount = ad.rewardAmount
            )
            adRepository.markWatched(ad.id)
            adStatsRepository.recordCompletion(ad.id, ad.title, totalAmount)

            val user = userRepository.user.value
            val fraudFlag = com.example.project_ad_wallet.domain.FraudDetector.checkForFraud(1, userActivityDao)
            userActivityRepository.logActivity(
                userName = user.name.ifBlank { "User" },
                activityType = "AD_WATCHED",
                details = "Ad: ${ad.title}",
                isFlagged = fraudFlag.isSuspicious,
                flagReason = fraudFlag.reason,
                userId = 1
            )

            _uiState.update {
                it.copy(
                    rewardGranted = true,
                    rewardAmount = totalAmount,
                    isComplete = false,
                )
            }
        }
    }

    fun dismissReward() {
        _uiState.update { it.copy(isComplete = true) }
    }

    fun skipAd() {
        val ad = _uiState.value.ad ?: return
        adRepository.markSkipped(ad.id)
    }
}
