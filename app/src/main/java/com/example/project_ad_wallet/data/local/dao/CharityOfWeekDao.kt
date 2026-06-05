package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharityOfWeekDao {
    @Query("SELECT * FROM charity_of_week WHERE id = 1")
    fun getCharityOfWeek(): Flow<CharityOfWeekEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCharityOfWeek(charity: CharityOfWeekEntity)

    @Query("""
        UPDATE charity_of_week
        SET totalDonatedThisWeek = totalDonatedThisWeek + :amount
        WHERE id = 1
    """)
    suspend fun addDonation(amount: Double)
}
