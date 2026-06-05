package com.example.project_ad_wallet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET walletBalance = walletBalance + :amount WHERE id = 1")
    suspend fun addToWalletBalance(amount: Double)

    @Query("UPDATE user_profile SET walletBalance = walletBalance - :amount WHERE id = 1")
    suspend fun deductFromWalletBalance(amount: Double)

    @Query("UPDATE user_profile SET profilePictureUri = :uri WHERE id = 1")
    suspend fun updateProfilePicture(uri: String)
}
