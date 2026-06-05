package com.example.project_ad_wallet.di

import android.content.Context
import androidx.room.Room
import com.example.project_ad_wallet.data.local.AdWalletDatabase
import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AdWalletDatabase {
        val db = Room.databaseBuilder(
            context,
            AdWalletDatabase::class.java,
            "adwallet_database"
        )
            .addMigrations(
                AdWalletDatabase.MIGRATION_1_2,
                AdWalletDatabase.MIGRATION_2_3,
                AdWalletDatabase.MIGRATION_3_4
            )
            .build()
        com.example.project_ad_wallet.AdWalletApp.database = db
        return db
    }

    @Provides
    fun provideEarningsDao(db: AdWalletDatabase): EarningsDao = db.earningsDao()

    @Provides
    fun provideDonationsDao(db: AdWalletDatabase): DonationsDao = db.donationsDao()

    @Provides
    fun provideUserProfileDao(db: AdWalletDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideAdsWatchedDao(db: AdWalletDatabase): AdsWatchedDao = db.adsWatchedDao()

    @Provides
    fun provideCharityOfWeekDao(db: AdWalletDatabase): CharityOfWeekDao =
        db.charityOfWeekDao()

    @Provides
    fun provideUserActivityDao(db: AdWalletDatabase): UserActivityDao = db.userActivityDao()
}
