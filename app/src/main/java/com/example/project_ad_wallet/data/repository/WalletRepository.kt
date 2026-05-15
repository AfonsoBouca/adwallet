package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.model.Earning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WalletRepository {

    companion object {
        val instance = WalletRepository()
    }

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _earnings = MutableStateFlow<List<Earning>>(emptyList())
    val earnings: StateFlow<List<Earning>> = _earnings.asStateFlow()

    fun addEarning(earning: Earning) {
        _earnings.update { it + earning }
        _balance.update { it + earning.amount }
    }

    fun deductBalance(amount: Double): Boolean {
        if (_balance.value < amount) return false
        _balance.update { it - amount }
        return true
    }

    fun reset() {
        _balance.value = 0.0
        _earnings.value = emptyList()
    }
}
