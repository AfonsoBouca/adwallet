package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.ui.settings.SettingsViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {

    private val userRepository = UserRepository.instance
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        userRepository.reset()
        userRepository.saveUser(User(name = "Alice", age = 28, gender = "Female"))
        userRepository.setThemeMode(ThemeMode.Dark)
        viewModel = SettingsViewModel()
    }

    @After
    fun tearDown() {
        userRepository.reset()
    }

    @Test
    fun logout_clearsUserProfileButPreservesTheme() {
        viewModel.logout()

        val user = userRepository.user.value
        assertEquals("", user.name)
        assertEquals(0, user.age)
        assertEquals("", user.gender)
        assertEquals(ThemeMode.Dark, userRepository.themeMode.value)
    }

    @Test
    fun deleteAccount_clearsAllUserData() {
        viewModel.deleteAccount()

        val user = userRepository.user.value
        assertEquals("", user.name)
        assertEquals(0, user.age)
        assertEquals("", user.gender)
        assertEquals(ThemeMode.System, userRepository.themeMode.value)
    }

    @Test
    fun deleteAccount_resetsWalletBalance() {
        val walletRepository = com.example.project_ad_wallet.data.repository.WalletRepository.instance
        walletRepository.addFunds(50.0)

        viewModel.deleteAccount()

        assertEquals(0.0, walletRepository.balance.value, 0.001)
    }

    @Test
    fun updateProfile_updatesUserRepositoryAndState() {
        viewModel.updateProfile("Bob", 30, "Male")

        val user = userRepository.user.value
        assertEquals("Bob", user.name)
        assertEquals(30, user.age)
        assertEquals("Male", user.gender)
    }

    @Test
    fun updateProfile_trimsName() {
        viewModel.updateProfile("  Alice  ", 28, "Female")

        val user = userRepository.user.value
        assertEquals("Alice", user.name)
    }
}
