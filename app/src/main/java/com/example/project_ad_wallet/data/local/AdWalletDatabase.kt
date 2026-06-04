package com.example.project_ad_wallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity

// TODO: substituir após merge PR #95
@Database(
    entities = [
        EarningEntity::class,
        DonationEntity::class,
        UserProfileEntity::class,
        AdWatchedEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AdWalletDatabase : RoomDatabase() {
    abstract fun earningsDao(): EarningsDao
    abstract fun donationsDao(): DonationsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun adsWatchedDao(): AdsWatchedDao
}
