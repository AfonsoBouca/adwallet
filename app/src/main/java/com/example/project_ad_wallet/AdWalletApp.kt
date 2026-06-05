package com.example.project_ad_wallet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AdWalletApp : Application() {
    companion object {
        lateinit var database: com.example.project_ad_wallet.data.local.AdWalletDatabase
    }
}
