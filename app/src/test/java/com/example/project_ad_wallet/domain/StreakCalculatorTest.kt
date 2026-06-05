package com.example.project_ad_wallet.domain

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class StreakCalculatorTest {

    private val today = System.currentTimeMillis()
    private val yesterday = today - TimeUnit.DAYS.toMillis(1)
    private val twoDaysAgo = today - TimeUnit.DAYS.toMillis(2)

    @Test
    fun streak_sameDay_doesNotIncrement() {
        val result = StreakCalculator.calculateNewStreak(
            currentStreak = 5,
            lastActiveDate = today
        )
        assertEquals(5, result)
    }

    @Test
    fun streak_consecutiveDay_increments() {
        val result = StreakCalculator.calculateNewStreak(
            currentStreak = 5,
            lastActiveDate = yesterday
        )
        assertEquals(6, result)
    }

    @Test
    fun streak_brokenAfterTwoDays_resetsToOne() {
        val result = StreakCalculator.calculateNewStreak(
            currentStreak = 10,
            lastActiveDate = twoDaysAgo
        )
        assertEquals(1, result)
    }

    @Test
    fun streakBonus_below3Days_isZero() {
        assertEquals(0.0, StreakCalculator.getStreakBonus(2), 0.001)
    }

    @Test
    fun streakBonus_3Days_is10Percent() {
        assertEquals(0.10, StreakCalculator.getStreakBonus(3), 0.001)
    }

    @Test
    fun streakBonus_7Days_is20Percent() {
        assertEquals(0.20, StreakCalculator.getStreakBonus(7), 0.001)
    }

    @Test
    fun streakBonus_14Days_is30Percent() {
        assertEquals(0.30, StreakCalculator.getStreakBonus(14), 0.001)
    }

    @Test
    fun streakBonus_30Days_is50Percent() {
        assertEquals(0.50, StreakCalculator.getStreakBonus(30), 0.001)
    }

    @Test
    fun isStreakBroken_afterTwoDays_isTrue() {
        assertTrue(StreakCalculator.isStreakBroken(twoDaysAgo))
    }

    @Test
    fun isStreakBroken_afterOneDay_isFalse() {
        assertFalse(StreakCalculator.isStreakBroken(yesterday))
    }
}
