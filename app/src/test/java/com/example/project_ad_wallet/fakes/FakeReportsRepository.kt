package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.repository.ReportsRepository

class FakeReportsRepository(
    val fakeEarningsDao: FakeEarningsDao = FakeEarningsDao(),
    val fakeDonationsDao: FakeDonationsDao = FakeDonationsDao(),
    val fakeUserActivityDao: FakeUserActivityDao = FakeUserActivityDao(),
    val fakeUserProfileDao: FakeUserProfileDao = FakeUserProfileDao()
) : ReportsRepository(fakeEarningsDao, fakeDonationsDao, fakeUserActivityDao, fakeUserProfileDao) {

    suspend fun setTotalEarned(amount: Double) {
        fakeEarningsDao.insertEarning(
            EarningEntity(
                id = 1L,
                adId = "ad_1",
                adTitle = "Ad Title",
                amount = amount,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
