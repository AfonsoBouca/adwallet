package com.example.project_ad_wallet.domain

import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus

object AdScheduler {
    fun filterAvailableAds(ads: List<Ad>): List<Ad> {
        return ads.filter { it.isActive && it.status == AdStatus.APPROVED }
    }
}
