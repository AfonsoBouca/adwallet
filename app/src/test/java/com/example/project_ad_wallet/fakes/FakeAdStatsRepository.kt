package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import com.example.project_ad_wallet.data.repository.AdStatsRepository

class FakeAdStatsRepository(
    private val dao: FakeAdStatsDao = FakeAdStatsDao()
) : AdStatsRepository(dao) {

    suspend fun addStats(stats: AdStatsEntity) {
        dao.upsertAdStats(stats)
    }
}
