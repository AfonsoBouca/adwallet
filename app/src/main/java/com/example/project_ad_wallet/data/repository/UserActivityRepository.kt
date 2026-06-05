package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserActivityRepository @Inject constructor(
    private val userActivityDao: UserActivityDao
) {
    fun getRecentActivity(): Flow<List<UserActivityEntity>> =
        userActivityDao.getRecentActivity()

    fun getFlaggedActivity(): Flow<List<UserActivityEntity>> =
        userActivityDao.getFlaggedActivity()

    suspend fun logActivity(
        userName: String,
        activityType: String,
        details: String = "",
        isFlagged: Boolean = false,
        flagReason: String? = null,
        userId: Int = 1
    ) {
        userActivityDao.insertActivity(
            UserActivityEntity(
                userId = userId,
                userName = userName,
                activityType = activityType,
                details = details,
                isFlagged = isFlagged,
                flagReason = flagReason
            )
        )
    }

    suspend fun flagActivity(id: Long, reason: String) {
        userActivityDao.flagActivity(id, reason)
    }
}
