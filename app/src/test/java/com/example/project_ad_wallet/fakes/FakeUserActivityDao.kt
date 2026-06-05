package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserActivityDao(
    private val adsLastHour: Int = 0,
    private val adsLastDay: Int = 0
) : UserActivityDao {
    private val activities = MutableStateFlow<List<UserActivityEntity>>(emptyList())

    override fun getRecentActivity(): Flow<List<UserActivityEntity>> = activities

    override fun getFlaggedActivity(): Flow<List<UserActivityEntity>> =
        activities.map { list -> list.filter { it.isFlagged } }

    override suspend fun insertActivity(activity: UserActivityEntity) {
        val current = activities.value.toMutableList()
        val newId = if (activity.id == 0L) (current.size + 1).toLong() else activity.id
        current.add(0, activity.copy(id = newId))
        activities.value = current
    }

    override suspend fun flagActivity(id: Long, reason: String) {
        val current = activities.value.map {
            if (it.id == id) it.copy(isFlagged = true, flagReason = reason) else it
        }
        activities.value = current
    }

    override suspend fun countAdsWatchedSince(userId: Int, since: Long): Int {
        val oneHourAgo = System.currentTimeMillis() - 3600_000L
        if (since >= oneHourAgo) {
            return adsLastHour + activities.value.count {
                it.userId == userId && it.activityType == "AD_WATCHED" && it.timestamp > since
            }
        }
        return adsLastDay + activities.value.count {
            it.userId == userId && it.activityType == "AD_WATCHED" && it.timestamp > since
        }
    }
}
