package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.ui.registration.RegistrationViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegistrationViewModelTest {

    private val userRepository = UserRepository.instance
    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setup() {
        userRepository.reset()
        viewModel = RegistrationViewModel(userRepository)
    }

    @After
    fun tearDown() {
        userRepository.reset()
    }

    @Test
    fun register_withValidData_updatesRepositoryAndState() {
        viewModel.onFullNameChanged("Test User")
        viewModel.onAgeChanged("25")
        viewModel.onGenderChanged("Male")
        viewModel.onEmailChanged("test@test.com")
        viewModel.onPasswordChanged("password")

        assertTrue(viewModel.isFormValid)

        viewModel.register()

        assertTrue(viewModel.uiState.value.isRegistered)
        val user = userRepository.user.value
        assertEquals("Test User", user.name)
        assertEquals(25, user.age)
        assertEquals("Male", user.gender)
    }

    @Test
    fun isFormValid_withMissingFields_returnsFalse() {
        viewModel.onFullNameChanged("Test User")
        viewModel.onAgeChanged("") // Missing age
        viewModel.onGenderChanged("Male")

        assertFalse(viewModel.isFormValid)
    }
}
