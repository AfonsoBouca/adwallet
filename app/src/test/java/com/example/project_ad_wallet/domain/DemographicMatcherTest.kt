package com.example.project_ad_wallet.domain

import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.model.DemographicTarget
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DemographicMatcherTest {

    private fun makeProfile(age: Int? = null, gender: String? = null) =
        UserProfileEntity(
            name = "Test",
            email = "test@test.com",
            age = age ?: 0,
            gender = gender ?: ""
        )

    @Test
    fun noTarget_matchesAllUsers() {
        val target = DemographicTarget()
        assertTrue(DemographicMatcher.matchesTarget(makeProfile(25, "Male"), target))
    }

    @Test
    fun ageTooLow_doesNotMatch() {
        val target = DemographicTarget(minAge = 18)
        assertFalse(DemographicMatcher.matchesTarget(makeProfile(16), target))
    }

    @Test
    fun ageInRange_matches() {
        val target = DemographicTarget(minAge = 18, maxAge = 35)
        assertTrue(DemographicMatcher.matchesTarget(makeProfile(25), target))
    }

    @Test
    fun ageTooHigh_doesNotMatch() {
        val target = DemographicTarget(maxAge = 30)
        assertFalse(DemographicMatcher.matchesTarget(makeProfile(35), target))
    }

    @Test
    fun genderNotInTarget_doesNotMatch() {
        val target = DemographicTarget(targetGenders = listOf("Female"))
        assertFalse(DemographicMatcher.matchesTarget(makeProfile(25, "Male"), target))
    }

    @Test
    fun genderInTarget_matches() {
        val target = DemographicTarget(targetGenders = listOf("Male", "Female"))
        assertTrue(DemographicMatcher.matchesTarget(makeProfile(25, "Male"), target))
    }

    @Test
    fun emptyGenderList_matchesAll() {
        val target = DemographicTarget(targetGenders = emptyList())
        assertTrue(DemographicMatcher.matchesTarget(makeProfile(25, "Male"), target))
    }
}
