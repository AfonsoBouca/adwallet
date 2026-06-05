package com.example.project_ad_wallet.di

import com.example.project_ad_wallet.data.local.dao.AdsWatchedDao
import com.example.project_ad_wallet.data.local.dao.DonationsDao
import com.example.project_ad_wallet.data.local.dao.EarningsDao
import com.example.project_ad_wallet.data.local.dao.UserProfileDao
import com.example.project_ad_wallet.data.local.dao.UserActivityDao
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.CharityRepository
import com.example.project_ad_wallet.data.repository.CharityOfWeekRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.ReportsRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.UserActivityRepository
import com.example.project_ad_wallet.data.repository.WalletRepository
import com.example.project_ad_wallet.data.repository.WithdrawalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserProfileDao): UserRepository =
        UserRepository(dao)

    @Provides
    @Singleton
    fun provideWalletRepository(
        earningsDao: EarningsDao,
        userProfileDao: UserProfileDao
    ): WalletRepository = WalletRepository(earningsDao, userProfileDao)

    @Provides
    @Singleton
    fun provideDonationRepository(
        donationsDao: DonationsDao,
        userProfileDao: UserProfileDao,
        charityOfWeekDao: com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
    ): DonationRepository = DonationRepository(
        donationsDao = donationsDao,
        userProfileDao = userProfileDao,
        charityOfWeekDao = charityOfWeekDao
    )

    @Provides
    @Singleton
    fun provideAdRepository(adsWatchedDao: AdsWatchedDao): AdRepository =
        AdRepository(adsWatchedDao)

    @Provides
    @Singleton
    fun provideCharityRepository(): CharityRepository = CharityRepository()

    @Provides
    @Singleton
    fun provideWithdrawalRepository(): WithdrawalRepository = WithdrawalRepository()

    @Provides
    @Singleton
    fun provideAuthRepository(): com.example.project_ad_wallet.data.auth.AuthRepository =
        com.example.project_ad_wallet.data.auth.AuthRepository()

    @Provides
    @Singleton
    fun provideCharityOfWeekRepository(
        dao: com.example.project_ad_wallet.data.local.dao.CharityOfWeekDao
    ): CharityOfWeekRepository = CharityOfWeekRepository(dao)

    @Provides
    @Singleton
    fun provideUserActivityRepository(dao: UserActivityDao): UserActivityRepository =
        UserActivityRepository(dao)

    @Provides
    @Singleton
    fun provideReportsRepository(
        earningsDao: EarningsDao,
        donationsDao: DonationsDao,
        userActivityDao: UserActivityDao,
        userProfileDao: UserProfileDao
    ): ReportsRepository = ReportsRepository(earningsDao, donationsDao, userActivityDao, userProfileDao)
}
