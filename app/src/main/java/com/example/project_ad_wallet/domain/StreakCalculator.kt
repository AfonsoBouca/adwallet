package com.example.project_ad_wallet.domain

import java.util.Calendar
import java.util.concurrent.TimeUnit

object StreakCalculator {

    fun calculateNewStreak(
        currentStreak: Int,
        lastActiveDate: Long,
        today: Long = System.currentTimeMillis()
    ): Int {
        if (lastActiveDate == 0L) return 1
        
        val lastDate = normalizeToDay(lastActiveDate)
        val todayDate = normalizeToDay(today)
        val diffDays = TimeUnit.MILLISECONDS.toDays(todayDate - lastDate)

        return when {
            diffDays == 0L -> currentStreak        // já viu hoje, mantém
            diffDays == 1L -> currentStreak + 1    // dia seguinte, incrementa
            else -> 1                               // quebrou a streak, recomeça
        }
    }

    fun isStreakBroken(lastActiveDate: Long, today: Long = System.currentTimeMillis()): Boolean {
        if (lastActiveDate == 0L) return false
        val diffDays = TimeUnit.MILLISECONDS.toDays(
            normalizeToDay(today) - normalizeToDay(lastActiveDate)
        )
        return diffDays > 1
    }

    fun getStreakBonus(streakDays: Int): Double {
        return when {
            streakDays >= 30 -> 0.50  // +50% bónus
            streakDays >= 14 -> 0.30  // +30% bónus
            streakDays >= 7  -> 0.20  // +20% bónus
            streakDays >= 3  -> 0.10  // +10% bónus
            else             -> 0.0   // sem bónus
        }
    }

    private fun normalizeToDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
