package com.example.project_ad_wallet.ui.components

import com.example.project_ad_wallet.ui.adFeed.AdFeedUiState
import org.junit.Assert.*
import org.junit.Test

class UiStateTest {

    @Test
    fun feedUiState_defaultValues_isLoadingFalse() {
        val state = AdFeedUiState()
        assertFalse(state.isLoading)
        assertTrue(state.ads.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun feedUiState_withError_hasErrorMessage() {
        val state = AdFeedUiState(error = "Erro de rede")
        assertEquals("Erro de rede", state.error)
    }

    @Test
    fun feedUiState_whileLoading_isLoadingTrue() {
        val state = AdFeedUiState(isLoading = true)
        assertTrue(state.isLoading)
    }
}
