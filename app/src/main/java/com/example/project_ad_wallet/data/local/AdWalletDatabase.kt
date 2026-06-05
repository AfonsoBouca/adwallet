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

import com.example.project_ad_wallet.data.local.dao.AdStatsDao
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity

@Database(
    entities = [
        EarningEntity::class,
        DonationEntity::class,
        UserProfileEntity::class,
        AdWatchedEntity::class,
        AdStatsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AdWalletDatabase : RoomDatabase() {
    abstract fun earningsDao(): EarningsDao
    abstract fun donationsDao(): DonationsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun adsWatchedDao(): AdsWatchedDao
    abstract fun adStatsDao(): AdStatsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `ad_stats` (
                        `adId` TEXT NOT NULL, 
                        `adTitle` TEXT NOT NULL, 
                        `totalViews` INTEGER NOT NULL, 
                        `totalCompletions` INTEGER NOT NULL, 
                        `totalSkips` INTEGER NOT NULL, 
                        `totalReports` INTEGER NOT NULL, 
                        `totalEarningsGenerated` REAL NOT NULL, 
                        `averageRating` REAL NOT NULL, 
                        `ratingCount` INTEGER NOT NULL, 
                        `createdAt` INTEGER NOT NULL, 
                        PRIMARY KEY(`adId`)
                    )
                """.trimIndent())
            }
        }
    }
}
