package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    @Query("SELECT * FROM user_activity ORDER BY timestamp DESC LIMIT 100")
    fun getRecentActivity(): Flow<List<UserActivityEntity>>

    @Query("SELECT * FROM user_activity WHERE isFlagged = 1 ORDER BY timestamp DESC")
    fun getFlaggedActivity(): Flow<List<UserActivityEntity>>

    @Insert
    suspend fun insertActivity(activity: UserActivityEntity)

    @Query("UPDATE user_activity SET isFlagged = 1, flagReason = :reason WHERE id = :id")
    suspend fun flagActivity(id: Long, reason: String)

    @Query("""
        SELECT COUNT(*) FROM user_activity
        WHERE userId = :userId
        AND activityType = 'AD_WATCHED'
        AND timestamp > :since
    """)
    suspend fun countAdsWatchedSince(userId: Int, since: Long): Int
}
