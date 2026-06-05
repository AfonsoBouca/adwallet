package com.example.project_ad_wallet.domain

import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.DemographicTarget

object DemographicMatcher {

    fun matchesTarget(
        userProfile: UserProfileEntity,
        target: DemographicTarget
    ): Boolean {
        val userAge = userProfile.age
        if (userAge != null) {
            if (target.minAge != null && userAge < target.minAge) return false
            if (target.maxAge != null && userAge > target.maxAge) return false
        }

        if (target.targetGenders.isNotEmpty()) {
            val userGender = userProfile.gender ?: return true
            if (!target.targetGenders.contains(userGender)) return false
        }

        return true
    }

    fun filterAdsForUser(
        ads: List<Ad>,
        userProfile: UserProfileEntity
    ): List<Ad> {
        return ads.filter { matchesTarget(userProfile, it.demographicTarget) }
    }
}
