package com.example.project_ad_wallet

import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.data.repository.CharityRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharityRepositoryTest {

    private lateinit var repo: CharityRepository

    @Before
    fun setup() {
        repo = CharityRepository()
    }

    @Test
    fun test_approveCharity_changesStatusToVerified() {
        val targetId = "scam-foundation"

        assertEquals(CharityStatus.PENDING, repo.getCharityById(targetId)?.status)

        repo.approveCharity(targetId)

        assertEquals(CharityStatus.VERIFIED, repo.getCharityById(targetId)?.status)
    }

    @Test
    fun test_rejectCharity_changesStatusToRejected() {
        val targetId = "scam-foundation"

        repo.rejectCharity(targetId)

        assertEquals(CharityStatus.REJECTED, repo.getCharityById(targetId)?.status)
    }
}