package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DonationsDao {
    @Query("SELECT * FROM donations ORDER BY timestamp DESC")
    fun getAllDonations(): Flow<List<DonationEntity>>

    @Insert
    suspend fun insertDonation(donation: DonationEntity)

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM donations")
    fun getTotalDonated(): Flow<Double>
}
