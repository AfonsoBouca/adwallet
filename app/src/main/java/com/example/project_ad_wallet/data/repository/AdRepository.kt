package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.R
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdRating
import com.example.project_ad_wallet.data.model.AdReport
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.model.Question
import com.example.project_ad_wallet.data.model.QuestionOption
import com.example.project_ad_wallet.data.model.QuestionnaireResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AdRepository {

    companion object {
        val instance = AdRepository()
    }

    private val _ads = MutableStateFlow(sampleAds())
    val ads: StateFlow<List<Ad>> = _ads.asStateFlow()

    private val _ratings = MutableStateFlow<List<AdRating>>(emptyList())
    val ratings: StateFlow<List<AdRating>> = _ratings.asStateFlow()

    private val _reports = MutableStateFlow<List<AdReport>>(emptyList())
    val reports: StateFlow<List<AdReport>> = _reports.asStateFlow()

    private val _questionnaireResponses = MutableStateFlow<List<QuestionnaireResponse>>(emptyList())
    val questionnaireResponses: StateFlow<List<QuestionnaireResponse>> = _questionnaireResponses.asStateFlow()

    private val _watchedAdIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedAdIds: StateFlow<Set<String>> = _watchedAdIds.asStateFlow()

    private val _skippedAdIds = MutableStateFlow<Set<String>>(emptySet())
    val skippedAdIds: StateFlow<Set<String>> = _skippedAdIds.asStateFlow()

    private val questionnaires: MutableMap<String, List<Question>> =
        sampleQuestionnaires().toMutableMap()

    fun getAdById(id: String): Ad? = _ads.value.firstOrNull { it.id == id }

    fun getQuestionsForAd(adId: String): List<Question> = questionnaires[adId] ?: emptyList()

    fun addQuestionnaire(adId: String, questions: List<Question>) {
        questionnaires[adId] = questions
    }

    fun addAd(ad: Ad) {
        _ads.update { it + ad }
    }

    // --- NOVAS FUNÇÕES PARA O ADMINISTRADOR ---
    fun approveAd(adId: String) {
        _ads.update { currentList ->
            currentList.map { ad ->
                if (ad.id == adId) ad.copy(status = AdStatus.APPROVED) else ad
            }
        }
    }

    fun rejectAd(adId: String) {
        _ads.update { currentList ->
            currentList.map { ad ->
                if (ad.id == adId) ad.copy(status = AdStatus.REJECTED) else ad
            }
        }
    }
    // ------------------------------------------

    fun addRating(rating: AdRating) {
        _ratings.update { it + rating }
    }

    fun addReport(report: AdReport) {
        _reports.update { it + report }
    }

    fun addQuestionnaireResponse(response: QuestionnaireResponse) {
        _questionnaireResponses.update { it + response }
    }

    fun markWatched(adId: String) {
        _watchedAdIds.update { it + adId }
    }

    fun markSkipped(adId: String) {
        _skippedAdIds.update { it + adId }
    }

    fun reset() {
        _ads.value = sampleAds()
        _ratings.value = emptyList()
        _reports.value = emptyList()
        _watchedAdIds.value = emptySet()
        _skippedAdIds.value = emptySet()
        _questionnaireResponses.value = emptyList()
    }
}

private fun sampleAds() = listOf(
    Ad(
        id = "1",
        title = "EcoBottle — Reusable Water Bottles",
        description = "Save the planet, one sip at a time.",
        thumbnailResId = R.drawable.ad_thumb_1,
        videoResId = R.raw.ad_video_1,
        rewardAmount = 0.10,
        advertiserName = "EcoBottle Ltd",
        category = "Sustainability",
        status = AdStatus.APPROVED, // Alterado aqui
    ),
    Ad(
        id = "2",
        title = "LearnFast — Online Courses",
        description = "Upskill in 10 minutes a day.",
        thumbnailResId = R.drawable.ad_thumb_2,
        videoResId = R.raw.ad_video_2,
        rewardAmount = 0.15,
        advertiserName = "LearnFast Inc",
        category = "Education",
        status = AdStatus.APPROVED, // Alterado aqui
    ),
    Ad(
        id = "3",
        title = "GreenEnergy — Solar Panels",
        description = "Power your home sustainably.",
        thumbnailResId = R.drawable.ad_thumb_3,
        videoResId = R.raw.ad_video_3,
        rewardAmount = 0.20,
        advertiserName = "GreenEnergy Co",
        category = "Energy",
        status = AdStatus.APPROVED, // Alterado aqui
    ),
    Ad( // Vamos criar um pendente falso para o Admin ter algo para aprovar
        id = "6",
        title = "Suspicious Crypto App",
        description = "Double your money in 5 minutes! No risks involved.",
        thumbnailResId = R.drawable.ad_thumb_4,
        videoResId = R.raw.ad_video_4,
        rewardAmount = 0.50,
        advertiserName = "CryptoBros LLC",
        category = "Other",
        status = AdStatus.PENDING, // Este vai aparecer para o Admin
    ),
)

private fun sampleQuestionnaires(): Map<String, List<Question>> = mapOf(
    "1" to listOf(
        Question(
            text = "How many plastic bottles does the average person use per year?",
            options = listOf(
                QuestionOption("50", false),
                QuestionOption("156", true),
                QuestionOption("300", false),
                QuestionOption("500", false),
            ),
        ),
        Question(
            text = "What is the main material of EcoBottle?",
            options = listOf(
                QuestionOption("Glass", false),
                QuestionOption("Stainless Steel", true),
                QuestionOption("BPA-free plastic", false),
                QuestionOption("Bamboo", false),
            ),
        ),
    ),
    "2" to listOf(
        Question(
            text = "How many minutes per day does LearnFast require?",
            options = listOf(
                QuestionOption("5", false),
                QuestionOption("10", true),
                QuestionOption("30", false),
                QuestionOption("60", false),
            ),
        ),
        Question(
            text = "Which skill is NOT available on LearnFast?",
            options = listOf(
                QuestionOption("Coding", false),
                QuestionOption("Cooking", false),
                QuestionOption("Piloting a plane", true),
                QuestionOption("Photography", false),
            ),
        ),
    ),
    "3" to listOf(
        Question(
            text = "What type of energy do solar panels generate?",
            options = listOf(
                QuestionOption("Wind energy", false),
                QuestionOption("Nuclear energy", false),
                QuestionOption("Electrical energy", true),
                QuestionOption("Thermal energy", false),
            ),
        ),
        Question(
            text = "How long is the typical warranty for GreenEnergy solar panels?",
            options = listOf(
                QuestionOption("5 years", false),
                QuestionOption("10 years", false),
                QuestionOption("25 years", true),
                QuestionOption("50 years", false),
            ),
        ),
    ),
    "4" to listOf(
        Question(
            text = "Which SDG does fighting hunger relate to?",
            options = listOf(
                QuestionOption("SDG 1", false),
                QuestionOption("SDG 2", true),
                QuestionOption("SDG 3", false),
                QuestionOption("SDG 4", false),
            ),
        ),
        Question(
            text = "How many people globally face food insecurity?",
            options = listOf(
                QuestionOption("100 million", false),
                QuestionOption("500 million", false),
                QuestionOption("800 million", true),
                QuestionOption("2 billion", false),
            ),
        ),
    ),
    "5" to listOf(
        Question(
            text = "What is the minimum age to join Onda Atlântica surf school?",
            options = listOf(
                QuestionOption("5 years", false),
                QuestionOption("8 years", true),
                QuestionOption("12 years", false),
                QuestionOption("16 years", false),
            ),
        ),
        Question(
            text = "Where is Onda Atlântica surf school located?",
            options = listOf(
                QuestionOption("Lisbon", false),
                QuestionOption("Algarve", false),
                QuestionOption("Porto", false),
                QuestionOption("Atlantic coast", true),
            ),
        ),
    ),
)
