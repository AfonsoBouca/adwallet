package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EarningsDao {
    @Query("SELECT * FROM earnings ORDER BY timestamp DESC")
    fun getAllEarnings(): Flow<List<EarningEntity>>

    @Insert
    suspend fun insertEarning(earning: EarningEntity)

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM earnings")
    fun getTotalEarned(): Flow<Double>
}
