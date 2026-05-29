package com.example.project_ad_wallet.ui.adWatch

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.model.Question
import com.example.project_ad_wallet.data.model.QuestionResult
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

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

class AdWatchViewModel(
    private val adRepository: AdRepository = AdRepository.instance,
    private val walletRepository: WalletRepository = WalletRepository.instance,
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
        val earning = Earning(
            id = UUID.randomUUID().toString(),
            adId = ad.id,
            adTitle = ad.title,
            amount = ad.rewardAmount,
        )
        walletRepository.addEarning(earning)
        adRepository.markWatched(ad.id)
        _uiState.update {
            it.copy(
                rewardGranted = true,
                rewardAmount = ad.rewardAmount,
                isComplete = false,
            )
        }
    }

    fun dismissReward() {
        _uiState.update { it.copy(isComplete = true) }
    }
}
