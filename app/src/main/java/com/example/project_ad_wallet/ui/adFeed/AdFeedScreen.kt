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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.example.project_ad_wallet.ui.components.AdCard
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.components.StreakWidget
import com.example.project_ad_wallet.ui.navigation.Routes

import com.example.project_ad_wallet.ui.components.AdWalletEmptyState
import com.example.project_ad_wallet.ui.components.AdWalletErrorState
import com.example.project_ad_wallet.ui.components.SkeletonItem
import androidx.compose.material.icons.filled.VideoLibrary

@Composable
fun AdFeedScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    onLogout: () -> Unit,
    viewModel: AdFeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val charityOfWeekViewModel: com.example.project_ad_wallet.ui.charities.CharityOfWeekViewModel = hiltViewModel()
    val charityOfWeek by charityOfWeekViewModel.charityOfWeek.collectAsState()

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
            AdWalletTopBar(
                title = "Home",
                balance = uiState.balance,
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
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
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(3) {
                            SkeletonItem()
                        }
                    }
                }
                uiState.error != null -> {
                    AdWalletErrorState(
                        message = uiState.error!!,
                        onRetry = { viewModel.retry() }
                    )
                }
                uiState.ads.isEmpty() -> {
                    AdWalletEmptyState(
                        icon = Icons.Default.VideoLibrary,
                        title = "Sem anúncios",
                        subtitle = "Não há anúncios disponíveis de momento.",
                        actionLabel = "Atualizar",
                        onAction = { viewModel.retry() }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            StreakWidget(
                                streakDays = uiState.streakDays,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (uiState.userRole == "USER" && charityOfWeek != null) {
                            item {
                                com.example.project_ad_wallet.ui.components.CharityOfWeekCard(
                                    charity = charityOfWeek!!,
                                    onDonateClick = {
                                        navController.navigate("charities?preselected=${charityOfWeek!!.charityId}")
                                    }
                                )
                            }
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
        }
    }
}
