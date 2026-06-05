package com.example.project_ad_wallet.domain

import com.example.project_ad_wallet.data.local.dao.UserActivityDao

object FraudDetector {

    private const val MAX_ADS_PER_HOUR = 20
    private const val MAX_ADS_PER_DAY = 100

    data class FraudFlag(
        val isSuspicious: Boolean,
        val reason: String?
    )

    suspend fun checkForFraud(
        userId: Int,
        activityDao: UserActivityDao
    ): FraudFlag {
        val oneHourAgo = System.currentTimeMillis() - 3600_000L
        val oneDayAgo = System.currentTimeMillis() - 86400_000L

        val adsLastHour = activityDao.countAdsWatchedSince(userId, oneHourAgo)
        val adsLastDay = activityDao.countAdsWatchedSince(userId, oneDayAgo)

        return when {
            adsLastHour > MAX_ADS_PER_HOUR ->
                FraudFlag(true, "Mais de $MAX_ADS_PER_HOUR anúncios na última hora")
            adsLastDay > MAX_ADS_PER_DAY ->
                FraudFlag(true, "Mais de $MAX_ADS_PER_DAY anúncios no último dia")
            else -> FraudFlag(false, null)
        }
    }
}
