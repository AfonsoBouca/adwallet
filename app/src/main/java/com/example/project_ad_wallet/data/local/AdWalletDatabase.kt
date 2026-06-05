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

import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity

@Database(
    entities = [
        EarningEntity::class,
        DonationEntity::class,
        UserProfileEntity::class,
        AdWatchedEntity::class,
        UserActivityEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AdWalletDatabase : RoomDatabase() {
    abstract fun earningsDao(): EarningsDao
    abstract fun donationsDao(): DonationsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun adsWatchedDao(): AdsWatchedDao
    abstract fun userActivityDao(): UserActivityDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `user_activity` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `userId` INTEGER NOT NULL, 
                        `userName` TEXT NOT NULL, 
                        `activityType` TEXT NOT NULL, 
                        `details` TEXT NOT NULL, 
                        `timestamp` INTEGER NOT NULL, 
                        `isFlagged` INTEGER NOT NULL, 
                        `flagReason` TEXT
                    )
                """.trimIndent())
            }
        }
    }
}
