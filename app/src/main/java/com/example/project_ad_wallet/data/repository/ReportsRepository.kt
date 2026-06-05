package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.model.PlatformReport
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ReportsRepository @Inject constructor(
    private val earningsDao: EarningsDao,
    private val donationsDao: DonationsDao,
    private val userActivityDao: UserActivityDao,
    private val userProfileDao: UserProfileDao
) {
    suspend fun generateReport(): PlatformReport {
        val totalEarned = earningsDao.getTotalEarned().first()
        val totalDonated = donationsDao.getTotalDonated().first()
        val allDonations = donationsDao.getAllDonations().first()
        val profile = userProfileDao.getProfileOnce()

        // Top charity por valor doado
        val topCharity = allDonations
            .groupBy { it.charityName }
            .mapValues { (_, donations) -> donations.sumOf { it.amount } }
            .maxByOrNull { it.value }

        return PlatformReport(
            totalAdsWatched = earningsDao.getAllEarnings().first().size,
            totalDonations = allDonations.size,
            totalAmountDonated = totalDonated,
            totalAmountEarned = totalEarned,
            averageStreakDays = profile?.streakDays?.toDouble() ?: 0.0,
            topCharityName = topCharity?.key ?: "N/A",
            topCharityDonations = topCharity?.value ?: 0.0
        )
    }
}
