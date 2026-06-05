package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.AdStatsDao
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AdStatsRepository @Inject constructor(
    private val adStatsDao: AdStatsDao
) {
    fun getAllAdStats(): Flow<List<AdStatsEntity>> =
        adStatsDao.getAllAdStats()

    fun getTotalViews(): Flow<Int> =
        adStatsDao.getTotalViews().map { it ?: 0 }

    fun getTotalCompletions(): Flow<Int> =
        adStatsDao.getTotalCompletions().map { it ?: 0 }

    suspend fun recordView(adId: String, adTitle: String) {
        // Garante que o registo existe antes de incrementar
        val existing = adStatsDao.getAdStats(adId).first()
        if (existing == null) {
            adStatsDao.upsertAdStats(AdStatsEntity(adId = adId, adTitle = adTitle))
        }
        adStatsDao.incrementViews(adId)
    }

    suspend fun recordCompletion(adId: String, adTitle: String, earnings: Double) {
        val existing = adStatsDao.getAdStats(adId).first()
        if (existing == null) {
            adStatsDao.upsertAdStats(AdStatsEntity(adId = adId, adTitle = adTitle))
        }
        adStatsDao.incrementCompletions(adId, earnings)
    }

    suspend fun recordRating(adId: String, adTitle: String, rating: Double) {
        val existing = adStatsDao.getAdStats(adId).first()
        if (existing == null) {
            adStatsDao.upsertAdStats(AdStatsEntity(adId = adId, adTitle = adTitle))
        }
        adStatsDao.addRating(adId, rating)
    }

    suspend fun clearAdStats() {
        adStatsDao.clearAdStats()
    }
}
