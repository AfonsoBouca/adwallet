package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdStatsDao {
    @Query("SELECT * FROM ad_stats ORDER BY totalViews DESC")
    fun getAllAdStats(): Flow<List<AdStatsEntity>>

    @Query("SELECT * FROM ad_stats WHERE adId = :adId")
    fun getAdStats(adId: String): Flow<AdStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAdStats(stats: AdStatsEntity)

    @Query("""
        UPDATE ad_stats SET
        totalViews = totalViews + 1
        WHERE adId = :adId
    """)
    suspend fun incrementViews(adId: String)

    @Query("""
        UPDATE ad_stats SET
        totalCompletions = totalCompletions + 1,
        totalEarningsGenerated = totalEarningsGenerated + :earnings
        WHERE adId = :adId
    """)
    suspend fun incrementCompletions(adId: String, earnings: Double)

    @Query("""
        UPDATE ad_stats SET
        averageRating = ((averageRating * ratingCount) + :newRating) / (ratingCount + 1),
        ratingCount = ratingCount + 1
        WHERE adId = :adId
    """)
    suspend fun addRating(adId: String, newRating: Double)

    @Query("SELECT SUM(totalViews) FROM ad_stats")
    fun getTotalViews(): Flow<Int?>

    @Query("SELECT SUM(totalCompletions) FROM ad_stats")
    fun getTotalCompletions(): Flow<Int?>

    @Query("DELETE FROM ad_stats")
    suspend fun clearAdStats()
}
