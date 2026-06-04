package com.example.project_ad_wallet.onboarding

import android.content.Context
import com.example.project_ad_wallet.data.model.onboardingPages
import com.example.project_ad_wallet.data.preferences.OnboardingPreferences
import com.example.project_ad_wallet.ui.onboarding.OnboardingViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingViewModelTest {

    @Test
    fun initialPage_isZero() {
        val viewModel = OnboardingViewModel(FakeOnboardingPreferences())
        assertEquals(0, viewModel.currentPage)
    }

    @Test
    fun nextPage_incrementsCurrentPage() {
        val viewModel = OnboardingViewModel(FakeOnboardingPreferences())
        viewModel.nextPage()
        assertEquals(1, viewModel.currentPage)
    }

    @Test
    fun previousPage_onFirstPage_doesNotGoNegative() {
        val viewModel = OnboardingViewModel(FakeOnboardingPreferences())
        viewModel.previousPage()
        assertEquals(0, viewModel.currentPage)
    }

    @Test
    fun isLastPage_onLastSlide_isTrue() {
        val viewModel = OnboardingViewModel(FakeOnboardingPreferences())
        repeat(onboardingPages.size - 1) { viewModel.nextPage() }
        assertTrue(viewModel.isLastPage)
    }

    @Test
    fun nextPage_onLastPage_doesNotExceedBounds() {
        val viewModel = OnboardingViewModel(FakeOnboardingPreferences())
        repeat(onboardingPages.size + 2) { viewModel.nextPage() }
        assertEquals(onboardingPages.size - 1, viewModel.currentPage)
    }
}

// Fake para testes (sem DataStore real)
class FakeOnboardingPreferences : OnboardingPreferences(mockk<Context>(relaxed = true)) {
    override val isOnboardingCompleted: Flow<Boolean> = flowOf(false)
    override suspend fun setOnboardingCompleted() {}
}
