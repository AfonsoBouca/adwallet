package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.AdStatsDao
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAdStatsDao : AdStatsDao {
    private val _stats = MutableStateFlow<Map<String, AdStatsEntity>>(emptyMap())

    override fun getAllAdStats(): Flow<List<AdStatsEntity>> =
        _stats.map { it.values.toList().sortedByDescending { stat -> stat.totalViews } }

    override fun getAdStats(adId: String): Flow<AdStatsEntity?> =
        _stats.map { it[adId] }

    override suspend fun upsertAdStats(stats: AdStatsEntity) {
        val current = _stats.value.toMutableMap()
        current[stats.adId] = stats
        _stats.value = current
    }

    override suspend fun incrementViews(adId: String) {
        val current = _stats.value.toMutableMap()
        val existing = current[adId] ?: AdStatsEntity(adId = adId, adTitle = "")
        current[adId] = existing.copy(totalViews = existing.totalViews + 1)
        _stats.value = current
    }

    override suspend fun incrementCompletions(adId: String, earnings: Double) {
        val current = _stats.value.toMutableMap()
        val existing = current[adId] ?: AdStatsEntity(adId = adId, adTitle = "")
        current[adId] = existing.copy(
            totalCompletions = existing.totalCompletions + 1,
            totalEarningsGenerated = existing.totalEarningsGenerated + earnings
        )
        _stats.value = current
    }

    override suspend fun addRating(adId: String, newRating: Double) {
        val current = _stats.value.toMutableMap()
        val existing = current[adId] ?: AdStatsEntity(adId = adId, adTitle = "")
        val newCount = existing.ratingCount + 1
        val newAvg = ((existing.averageRating * existing.ratingCount) + newRating) / newCount
        current[adId] = existing.copy(
            averageRating = newAvg,
            ratingCount = newCount
        )
        _stats.value = current
    }

    override fun getTotalViews(): Flow<Int?> =
        _stats.map { it.values.sumOf { stat -> stat.totalViews } }

    override fun getTotalCompletions(): Flow<Int?> =
        _stats.map { it.values.sumOf { stat -> stat.totalCompletions } }

    override suspend fun clearAdStats() {
        _stats.value = emptyMap()
    }
}
