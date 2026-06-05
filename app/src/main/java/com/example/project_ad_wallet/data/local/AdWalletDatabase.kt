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
import com.example.project_ad_wallet.data.local.entities.AdWatchedEntity
import com.example.project_ad_wallet.data.local.entities.DonationEntity
import com.example.project_ad_wallet.data.local.entities.EarningEntity
import com.example.project_ad_wallet.data.local.entities.UserProfileEntity
import com.example.project_ad_wallet.data.local.entities.CharityOfWeekEntity

@Database(
    entities = [
        EarningEntity::class,
        DonationEntity::class,
        UserProfileEntity::class,
        AdWatchedEntity::class,
        CharityOfWeekEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AdWalletDatabase : RoomDatabase() {
    abstract fun earningsDao(): EarningsDao
    abstract fun donationsDao(): DonationsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun adsWatchedDao(): AdsWatchedDao
    abstract fun charityOfWeekDao(): CharityOfWeekDao

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
    }
}

