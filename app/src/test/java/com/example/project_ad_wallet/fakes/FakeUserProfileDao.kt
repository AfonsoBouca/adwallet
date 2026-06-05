package com.example.project_ad_wallet.fakes

import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserProfileDao : UserProfileDao {
    private val _profile = MutableStateFlow<UserProfileEntity?>(null)

    override fun getProfile(): Flow<UserProfileEntity?> = _profile

    override suspend fun getProfileOnce(): UserProfileEntity? = _profile.value

    override suspend fun upsertProfile(profile: UserProfileEntity) {
        _profile.value = profile
    }

    override suspend fun addToWalletBalance(amount: Double) {
        val current = _profile.value ?: UserProfileEntity(id = 1, name = "User", email = "")
        _profile.value = current.copy(walletBalance = current.walletBalance + amount)
    }

    override suspend fun deductFromWalletBalance(amount: Double) {
        val current = _profile.value ?: return
        _profile.value = current.copy(walletBalance = current.walletBalance - amount)
    }

    override suspend fun updateProfilePicture(uri: String) {
        val current = _profile.value ?: return
        _profile.value = current.copy(profilePictureUri = uri)
    }
}
