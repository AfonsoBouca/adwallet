package com.example.project_ad_wallet.data.repository

import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.domain.StreakCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalletRepository(
    private val earningsDao: EarningsDao? = null,
    private val userProfileDao: UserProfileDao? = null,
    private val externalScope: CoroutineScope? = null
) {

    companion object {
        private var _instance: WalletRepository? = null
        val instance: WalletRepository
            get() = _instance ?: WalletRepository()

        fun initialize(earningsDao: EarningsDao, userProfileDao: UserProfileDao) {
            _instance = WalletRepository(earningsDao, userProfileDao)
        }
    }

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _earnings = MutableStateFlow<List<Earning>>(emptyList())
    val earnings: StateFlow<List<Earning>> = _earnings.asStateFlow()

    private val repositoryScope = externalScope ?: CoroutineScope(Dispatchers.IO)

    init {
        userProfileDao?.getProfile()?.onEach { entity ->
            if (entity != null) {
                _balance.value = entity.walletBalance
            }
        }?.launchIn(repositoryScope)

        earningsDao?.getAllEarnings()?.onEach { entities ->
            _earnings.value = entities.map { entity ->
                Earning(
                    id = entity.id.toString(),
                    adId = entity.adId,
                    adTitle = entity.adTitle,
                    amount = entity.amount,
                    timestamp = entity.timestamp
                )
            }
        }?.launchIn(repositoryScope)
    }

    fun addEarning(earning: Earning) {
        repositoryScope.launch {
            earningsDao?.insertEarning(
                EarningEntity(
                    adId = earning.adId,
                    adTitle = earning.adTitle,
                    amount = earning.amount,
                    timestamp = earning.timestamp
                )
            )
            userProfileDao?.addToWalletBalance(earning.amount)
        }
        // Update in‑memory balance for tests without a DAO
        _balance.update { it + earning.amount }
    }

    fun addFunds(amount: Double) {
        repositoryScope.launch {
            userProfileDao?.addToWalletBalance(amount)
        }
        // Update in‑memory balance for tests without a DAO
        _balance.update { it + amount }
    }

    suspend fun completeAdAndUpdateStreak(
        adTitle: String,
        adId: String,
        baseAmount: Double
    ): Double {
        val profile = userProfileDao?.getProfileOnce()
        val newStreak = StreakCalculator.calculateNewStreak(
            currentStreak = profile?.streakDays ?: 0,
            lastActiveDate = profile?.lastActiveDate ?: 0L
        )
        val bonus = StreakCalculator.getStreakBonus(newStreak)
        val totalAmount = baseAmount + (baseAmount * bonus)

        userProfileDao?.upsertProfile(
            profile?.copy(
                streakDays = newStreak,
                lastActiveDate = System.currentTimeMillis(),
                walletBalance = (profile.walletBalance) + totalAmount
            ) ?: UserProfileEntity(
                name = "User",
                email = "",
                streakDays = newStreak,
                lastActiveDate = System.currentTimeMillis(),
                walletBalance = totalAmount
            )
        )

        earningsDao?.insertEarning(
            EarningEntity(adTitle = adTitle, adId = adId, amount = totalAmount)
        )
        return totalAmount
    }

    fun generateRandomHistory(charities: List<com.example.project_ad_wallet.data.model.Charity>) {
        val randomEarnings = (1..5).map { i ->
            Earning(
                id = "rand_e_$i",
                adId = "ad_$i",
                adTitle = "Random Ad $i",
                amount = (0.10..0.50).random(),
                timestamp = System.currentTimeMillis() - (i * 3600000)
            )
        }
        
        repositoryScope.launch {
            randomEarnings.forEach { earning ->
                earningsDao?.insertEarning(
                    EarningEntity(
                        adId = earning.adId,
                        adTitle = earning.adTitle,
                        amount = earning.amount,
                        timestamp = earning.timestamp
                    )
                )
            }
            val totalEarned = randomEarnings.sumOf { it.amount }
            userProfileDao?.addToWalletBalance(totalEarned)
        }
    }

    private fun ClosedRange<Double>.random() = 
        java.util.concurrent.ThreadLocalRandom.current().nextDouble(start, endInclusive)

    fun deductBalance(amount: Double): Boolean {
        if (_balance.value < amount) return false
        repositoryScope.launch {
            userProfileDao?.deductFromWalletBalance(amount)
        }
        return true
    }

    fun reset() {
        _balance.value = 0.0
        _earnings.value = emptyList()
        // In a real app, clear DB
    }
}
