package com.example.project_ad_wallet.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project_ad_wallet.data.local.AdWalletDatabase
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var db: AdWalletDatabase
    private lateinit var earningsDao: EarningsDao
    private lateinit var donationsDao: DonationsDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AdWalletDatabase::class.java
        ).allowMainThreadQueries().build()
        earningsDao = db.earningsDao()
        donationsDao = db.donationsDao()
    }

    @After
    fun teardown() { db.close() }

    @Test
    fun insertEarning_andRetrieve_returnsCorrectData() = runTest {
        val earning = EarningEntity(adTitle = "Test Ad", adId = "ad_1", amount = 0.50)
        earningsDao.insertEarning(earning)
        val result = earningsDao.getAllEarnings().first()
        assert(result.isNotEmpty())
        assertEquals("Test Ad", result[0].adTitle)
    }

    @Test
    fun insertDonation_andRetrieve_returnsCorrectData() = runTest {
        val donation = DonationEntity(charityName = "UNICEF", charityId = "ch_1", amount = 2.00)
        donationsDao.insertDonation(donation)
        val result = donationsDao.getAllDonations().first()
        assert(result.isNotEmpty())
        assertEquals("UNICEF", result[0].charityName)
    }

    @Test
    fun totalEarned_withNoData_returnsZero() = runTest {
        val total = earningsDao.getTotalEarned().first()
        assertEquals(0.0, total, 0.001)
    }

    @Test
    fun walletBalance_afterAddAndDeduct_isCorrect() = runTest {
        db.userProfileDao().upsertProfile(
            UserProfileEntity(name = "Test", email = "test@adwallet.com")
        )
        db.userProfileDao().addToWalletBalance(5.00)
        db.userProfileDao().deductFromWalletBalance(2.00)
        val profile = db.userProfileDao().getProfile().first()
        assertEquals(3.00, profile?.walletBalance ?: 0.0, 0.001)
    }
}
