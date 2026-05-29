package com.example.project_ad_wallet.ui.advertiserUpload

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.R
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class AdvertiserUploadUiState(
    val title: String = "",
    val companyName: String = "",
    val category: String = "",
    val description: String = "",
    val rewardAmount: String = "",
    val durationSeconds: String = "",
    val selectedThumbnailIndex: Int = 0,
    val selectedVideoIndex: Int = 0,
    val titleError: String? = null,
    val companyNameError: String? = null,
    val categoryError: String? = null,
    val descriptionError: String? = null,
    val rewardAmountError: String? = null,
    val durationSecondsError: String? = null,
    val showConfirmDialog: Boolean = false,
    val isSubmitted: Boolean = false,
)

val THUMBNAIL_RES_IDS = listOf(
    R.drawable.ad_thumb_1,
    R.drawable.ad_thumb_2,
    R.drawable.ad_thumb_3,
    R.drawable.ad_thumb_4,
    R.drawable.ad_thumb_5,
)

val VIDEO_RES_IDS = listOf(
    R.raw.ad_video_1,
    R.raw.ad_video_2,
    R.raw.ad_video_3,
    R.raw.ad_video_4,
    R.raw.ad_video_5,
)

val AD_CATEGORIES = listOf(
    "Education", "Sustainability", "Energy", "Charity", "Sports", "Other"
)

class AdvertiserUploadViewModel(
    private val adRepository: AdRepository = AdRepository.instance,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdvertiserUploadUiState())
    val uiState: StateFlow<AdvertiserUploadUiState> = _uiState.asStateFlow()

    fun onTitleChanged(value: String) = _uiState.update { it.copy(title = value, titleError = null) }
    fun onCompanyNameChanged(value: String) = _uiState.update { it.copy(companyName = value, companyNameError = null) }
    fun onCategoryChanged(value: String) = _uiState.update { it.copy(category = value, categoryError = null) }
    fun onDescriptionChanged(value: String) {
        if (value.length <= 200) _uiState.update { it.copy(description = value, descriptionError = null) }
    }
    fun onRewardAmountChanged(value: String) = _uiState.update { it.copy(rewardAmount = value, rewardAmountError = null) }
    fun onDurationSecondsChanged(value: String) = _uiState.update { it.copy(durationSeconds = value, durationSecondsError = null) }

    fun cycleThumbnail() {
        _uiState.update { it.copy(selectedThumbnailIndex = (it.selectedThumbnailIndex + 1) % THUMBNAIL_RES_IDS.size) }
    }

    fun cycleVideo() {
        _uiState.update { it.copy(selectedVideoIndex = (it.selectedVideoIndex + 1) % VIDEO_RES_IDS.size) }
    }

    fun submitAd() {
        val state = _uiState.value
        var hasError = false

        val titleError = if (state.title.isBlank()) "Title is required" else null
        val companyError = if (state.companyName.isBlank()) "Company name is required" else null
        val categoryError = if (state.category.isBlank()) "Category is required" else null
        val descError = if (state.description.isBlank()) "Description is required" else null
        val reward = state.rewardAmount.toDoubleOrNull()
        val rewardError = when {
            state.rewardAmount.isBlank() -> "Reward amount is required"
            reward == null -> "Enter a valid number"
            reward < 0.01 -> "Minimum reward is \$0.01"
            reward > 1.00 -> "Maximum reward is \$1.00"
            else -> null
        }
        val duration = state.durationSeconds.toIntOrNull()
        val durationError = when {
            state.durationSeconds.isBlank() -> "Duration is required"
            duration == null -> "Enter a valid number"
            duration <= 0 -> "Duration must be greater than zero"
            else -> null
        }

        hasError = listOf(titleError, companyError, categoryError, descError, rewardError, durationError).any { it != null }

        _uiState.update {
            it.copy(
                titleError = titleError,
                companyNameError = companyError,
                categoryError = categoryError,
                descriptionError = descError,
                rewardAmountError = rewardError,
                durationSecondsError = durationError,
                showConfirmDialog = !hasError,
            )
        }
    }

    fun confirmUpload() {
        val state = _uiState.value
        val reward = state.rewardAmount.toDoubleOrNull() ?: return
        val duration = state.durationSeconds.toIntOrNull() ?: return
        val ad = Ad(
            id = UUID.randomUUID().toString(),
            title = state.title.trim(),
            description = state.description.trim(),
            thumbnailResId = THUMBNAIL_RES_IDS[state.selectedThumbnailIndex],
            videoResId = VIDEO_RES_IDS[state.selectedVideoIndex],
            rewardAmount = reward,
            advertiserName = state.companyName.trim(),
            category = state.category,
            durationSeconds = duration,
            isActive = false,
        )
        adRepository.addAd(ad)
        _uiState.update { it.copy(showConfirmDialog = false, isSubmitted = true) }
    }

    fun cancelUpload() {
        _uiState.update { AdvertiserUploadUiState() }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmDialog = false) }
    }
}