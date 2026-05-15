package com.example.project_ad_wallet.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun AdWatchPlaceholder(adId: String, navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ad Watch — coming in feature/25-watch-ads")
    }
}

@Composable
fun AdRatingPlaceholder(adId: String, navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ad Rating — coming in feature/42-rate-ads")
    }
}

@Composable
fun NotificationPrefsPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notification Preferences — coming in feature/49-notification-prefs")
    }
}

@Composable
fun AdvertiserUploadPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Advertiser Upload — coming in feature/54-advertiser-upload")
    }
}

@Composable
fun EarningsHistoryPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Earnings History — coming in feature/44-earnings-history")
    }
}

@Composable
fun DonationHistoryPlaceholder(navController: NavHostController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Donation History — coming in feature/53-donation-history")
    }
}
