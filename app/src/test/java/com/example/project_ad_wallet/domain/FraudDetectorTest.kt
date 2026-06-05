package com.example.project_ad_wallet.domain

import com.example.project_ad_wallet.fakes.FakeUserActivityDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FraudDetectorTest {

    @Test
    fun noActivity_isNotSuspicious() = runTest {
        val fakeDao = FakeUserActivityDao()
        val result = FraudDetector.checkForFraud(1, fakeDao)
        assertFalse(result.isSuspicious)
    }

    @Test
    fun tooManyAdsPerHour_isSuspicious() = runTest {
        val fakeDao = FakeUserActivityDao(adsLastHour = 25)
        val result = FraudDetector.checkForFraud(1, fakeDao)
        assertTrue(result.isSuspicious)
        assertNotNull(result.reason)
    }

    @Test
    fun tooManyAdsPerDay_isSuspicious() = runTest {
        val fakeDao = FakeUserActivityDao(adsLastDay = 110)
        val result = FraudDetector.checkForFraud(1, fakeDao)
        assertTrue(result.isSuspicious)
    }
}
