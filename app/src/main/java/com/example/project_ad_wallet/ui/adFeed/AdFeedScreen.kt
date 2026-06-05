package com.example.project_ad_wallet.ui.adFeed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.example.project_ad_wallet.ui.components.AdCard
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.components.StreakWidget
import com.example.project_ad_wallet.ui.navigation.Routes

@Composable
fun AdFeedScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    viewModel: AdFeedViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showStreakBrokenNotification) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissStreakBrokenNotification() },
            title = { Text("Streak Interrompida") },
            text = { Text("A tua streak foi interrompida. Começa de novo hoje! 💪") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissStreakBrokenNotification() }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AdWalletTopBar(title = "Home", balance = uiState.balance)
        },
        bottomBar = {
            AdWalletBottomBar(navController = navController, items = bottomNavItems)
        },
        floatingActionButton = {
            androidx.compose.foundation.layout.Column {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Routes.ADVERTISER_UPLOAD) },
                    icon = { Icon(Icons.Default.Campaign, contentDescription = null) },
                    text = { Text("Advertiser? Upload Ad") },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                )

                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Routes.ADVERTISER_RESPONSES) },
                    icon = { Icon(Icons.Default.Campaign, contentDescription = null) },
                    text = { Text("Advertiser? Responses") },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("advertiser_responses_button"),
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                StreakWidget(
                    streakDays = uiState.streakDays,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(uiState.ads, key = { it.id }) { ad ->
                AdCard(
                    ad = ad,
                    isWatched = ad.id in uiState.watchedAdIds,
                    onClick = { navController.navigate(Routes.adWatch(ad.id)) },
                )
            }
        }
    }
}
