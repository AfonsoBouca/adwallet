package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAdsWatchedDao : AdsWatchedDao {
    private val _watched = MutableStateFlow<List<AdWatchedEntity>>(emptyList())

    override fun getAllWatched(): Flow<List<AdWatchedEntity>> = _watched

    override suspend fun insertOrUpdate(adWatched: AdWatchedEntity) {
        val currentList = _watched.value.toMutableList()
        val index = currentList.indexOfFirst { it.adId == adWatched.adId }
        if (index >= 0) {
            currentList[index] = adWatched
        } else {
            currentList.add(adWatched)
        }
        _watched.value = currentList
    }

    override suspend fun getExcludedAdIds(): List<String> {
        return _watched.value.filter { it.isReported || it.isSkipped }.map { it.adId }
    }
}
