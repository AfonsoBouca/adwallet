package com.example.project_ad_wallet.ui.adRating

import androidx.lifecycle.ViewModel
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdRating
import com.example.project_ad_wallet.data.repository.AdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AdRatingUiState(
    val ad: Ad? = null,
    val selectedRating: Int = 0,
    val isSubmitted: Boolean = false,
    val isSkipped: Boolean = false,
)

@HiltViewModel
class AdRatingViewModel @Inject constructor(
    private val adRepository: AdRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdRatingUiState())
    val uiState: StateFlow<AdRatingUiState> = _uiState.asStateFlow()

    fun loadAd(adId: String) {
        val ad = adRepository.getAdById(adId)
        _uiState.update { it.copy(ad = ad) }
    }

    fun selectRating(rating: Int) {
        if (rating in 1..5) {
            _uiState.update { it.copy(selectedRating = rating) }
        }
    }

    fun submitRating() {
        val state = _uiState.value
        val ad = state.ad ?: return
        if (state.selectedRating !in 1..5) return

        adRepository.addRating(
            AdRating(adId = ad.id, rating = state.selectedRating)
        )
        _uiState.update { it.copy(isSubmitted = true) }
    }

    fun skipRating() {
        _uiState.update { it.copy(isSkipped = true) }
    }
}
