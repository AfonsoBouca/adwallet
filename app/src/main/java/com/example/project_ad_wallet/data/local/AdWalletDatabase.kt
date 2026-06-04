package com.example.project_ad_wallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity

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

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // reservado para migrações futuras
            }
        }
    }
}
