package com.example.project_ad_wallet.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltModuleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var walletRepository: WalletRepository

    @Inject
    lateinit var adRepository: AdRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun userRepository_isInjected() {
        assertNotNull(userRepository)
    }

    @Test
    fun walletRepository_isInjected() {
        assertNotNull(walletRepository)
    }

    @Test
    fun adRepository_isInjected() {
        assertNotNull(adRepository)
    }
}
