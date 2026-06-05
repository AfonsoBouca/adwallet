package com.example.project_ad_wallet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import javax.inject.Inject
import com.example.project_ad_wallet.data.local.AdWalletDatabase

@HiltAndroidApp
class AdWalletApp : Application() {
    @Inject
    lateinit var database: AdWalletDatabase
}
