package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeEarningsDao : EarningsDao {
    private val _earnings = MutableStateFlow<List<EarningEntity>>(emptyList())

    override fun getAllEarnings(): Flow<List<EarningEntity>> = _earnings

    override suspend fun insertEarning(earning: EarningEntity) {
        val currentList = _earnings.value.toMutableList()
        currentList.add(0, earning)
        _earnings.value = currentList
    }

    override fun getTotalEarned(): Flow<Double> {
        return _earnings.map { it.sumOf { earning -> earning.amount } }
    }
}
