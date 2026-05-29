package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.data.repository.AdRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdRepositoryTest {

    private lateinit var repo: AdRepository

    @Before
    fun setup() {
        repo = AdRepository()
    }

    @Test
    fun test_approveAd_changesStatusToApproved() {
        val targetId = "6"
        
        val initialAd = repo.getAdById(targetId)
        assertEquals(AdStatus.PENDING, initialAd?.status)

        repo.approveAd(targetId)

        val updatedAd = repo.getAdById(targetId)
        assertEquals(AdStatus.APPROVED, updatedAd?.status)
    }

    @Test
    fun test_rejectAd_changesStatusToRejected() {
        val targetId = "6"

        repo.rejectAd(targetId)

        val updatedAd = repo.getAdById(targetId)
        assertEquals(AdStatus.REJECTED, updatedAd?.status)
    }
}