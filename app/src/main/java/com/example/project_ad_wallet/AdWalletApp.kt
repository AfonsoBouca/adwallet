package com.example.project_ad_wallet

import android.app.Application
import androidx.room.Room
import com.example.project_ad_wallet.data.local.AdWalletDatabase
import com.example.project_ad_wallet.data.repository.AdRepository
import com.example.project_ad_wallet.data.repository.DonationRepository
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.data.repository.WalletRepository

class AdWalletApp : Application() {

    lateinit var database: AdWalletDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AdWalletDatabase::class.java,
            "adwallet-db"
        ).build()

        // Initialize repositories with DAOs
        UserRepository.initialize(database.userProfileDao())
        WalletRepository.initialize(database.earningsDao(), database.userProfileDao())
        DonationRepository.initialize(database.donationsDao(), database.userProfileDao())
        AdRepository.initialize(database.adsWatchedDao())
    }
}
