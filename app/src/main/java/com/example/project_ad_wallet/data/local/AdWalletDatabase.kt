package com.example.project_ad_wallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity

@Database(
    entities = [
        EarningEntity::class,
        DonationEntity::class,
        UserProfileEntity::class,
        AdWatchedEntity::class,
        CharityOfWeekEntity::class,
        UserActivityEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AdWalletDatabase : RoomDatabase() {
    abstract fun earningsDao(): EarningsDao
    abstract fun donationsDao(): DonationsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun adsWatchedDao(): AdsWatchedDao
    abstract fun charityOfWeekDao(): CharityOfWeekDao
    abstract fun userActivityDao(): UserActivityDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS charity_of_week (
                        id INTEGER PRIMARY KEY NOT NULL DEFAULT 1,
                        charityId TEXT NOT NULL,
                        charityName TEXT NOT NULL,
                        charityDescription TEXT NOT NULL,
                        sdgNumber INTEGER NOT NULL,
                        sdgDescription TEXT NOT NULL,
                        logoRes TEXT NOT NULL,
                        weekStartTimestamp INTEGER NOT NULL,
                        totalDonatedThisWeek REAL NOT NULL DEFAULT 0.0
                    )
                """)
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
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

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE earnings ADD COLUMN baseAmount REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE earnings ADD COLUMN streakBonus REAL NOT NULL DEFAULT 0.0")
                database.execSQL("UPDATE earnings SET baseAmount = amount")
            }
        }
    }
}
