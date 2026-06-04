package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdsWatchedDao {
    @Query("SELECT * FROM ads_watched")
    fun getAllWatched(): Flow<List<AdWatchedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(adWatched: AdWatchedEntity)

    @Query("SELECT adId FROM ads_watched WHERE isReported = 1 OR isSkipped = 1")
    suspend fun getExcludedAdIds(): List<String>
}
