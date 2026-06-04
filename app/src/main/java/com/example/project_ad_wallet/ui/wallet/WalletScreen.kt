package com.example.project_ad_wallet.ui.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.data.model.Donation
import com.example.project_ad_wallet.data.model.Earning
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.components.EmptyStateView
import com.example.project_ad_wallet.ui.components.asCurrency
import com.example.project_ad_wallet.ui.navigation.Routes
import com.example.project_ad_wallet.ui.withdrawals.toDisplayInfo

import com.example.project_ad_wallet.ui.components.AdWalletEmptyState
import com.example.project_ad_wallet.ui.components.AdWalletErrorState
import com.example.project_ad_wallet.ui.components.AdWalletLoadingState
import androidx.compose.material.icons.filled.Favorite

@Composable
fun WalletScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    viewModel: WalletViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Balance", "Earnings", "Donations", "Withdrawals")

    Scaffold(
        topBar = { AdWalletTopBar(title = "Wallet", balance = uiState.balance) },
        bottomBar = { AdWalletBottomBar(navController = navController, items = bottomNavItems) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> AdWalletLoadingState()
                uiState.error != null -> AdWalletErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.retry() }
                )
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        ScrollableTabRow(selectedTabIndex = selectedTab) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(title) },
                                )
                            }
                        }

                        when (selectedTab) {
                            0 -> BalanceTab(uiState) { navController.navigate(Routes.withdrawalRequest()) }
                            1 -> EarningsTab(uiState.earnings, uiState.totalEarned)
                            2 -> DonationsTab(uiState.donations, uiState.totalDonated, navController)
                            3 -> WithdrawalsTab(uiState.withdrawals)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceTab(uiState: WalletUiState, onRequestWithdrawal: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp),
        )
        Text(
            text = "Current Balance",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = uiState.balance.asCurrency(),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StatCard("Total Earned", uiState.totalEarned.asCurrency())
            StatCard("Total Donated", uiState.totalDonated.asCurrency())
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRequestWithdrawal,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Request Withdrawal")
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EarningsTab(earnings: List<Earning>, totalEarned: Double) {
    if (earnings.isEmpty()) {
        AdWalletEmptyState(
            icon = Icons.Default.AccountBalanceWallet,
            title = "Ainda não ganhaste saldo.",
            subtitle = "Vê anúncios para começar a ganhar."
        )
    } else {
        LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text(
                    text = "Total earned: ${totalEarned.asCurrency()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            items(earnings) { earning ->
                EarningRow(earning)
            }
        }
    }
}

@Composable
private fun EarningRow(earning: Earning) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = earning.adTitle,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = formatRelativeTime(earning.timestamp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "+${earning.amount.asCurrency()}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DonationsTab(donations: List<Donation>, totalDonated: Double, navController: NavHostController) {
    if (donations.isEmpty()) {
        AdWalletEmptyState(
            icon = Icons.Default.Favorite,
            title = "Ainda não fizeste nenhuma doação.",
            subtitle = "Doa o teu saldo a uma causa que te importa.",
            actionLabel = "Donate to Charities",
            onAction = { navController.navigate(Routes.CHARITIES) }
        )
    } else {
        LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total donated: ${totalDonated.asCurrency()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Button(
                        onClick = { navController.navigate(Routes.CHARITIES) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.testTag("donate_button_wallet")
                    ) {
                        Text("Donate")
                    }
                }
            }
            items(donations) { donation ->
                DonationRow(donation)
            }
        }
    }
}

@Composable
private fun DonationRow(donation: Donation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = donation.charityName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                ) {
                    Text(
                        text = "SDG ${donation.sdgNumber} — ${donation.sdgName}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRelativeTime(donation.timestamp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = donation.amount.asCurrency(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} minutes ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        diff < 172_800_000 -> "Yesterday"
        else -> java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            .format(java.util.Date(timestamp))
    }
}

@Composable
private fun WithdrawalsTab(withdrawals: List<WithdrawalRequest>) {
    if (withdrawals.isEmpty()) {
        EmptyStateView(
            icon = Icons.Default.AccountBalanceWallet,
            message = "No withdrawal requests yet",
        )
    } else {
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(withdrawals) { withdrawal ->
                WithdrawalRow(withdrawal)
            }
        }
    }
}

@Composable
private fun WithdrawalRow(withdrawal: WithdrawalRequest) {
    val statusInfo = withdrawal.status.toDisplayInfo()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = withdrawal.amount.asCurrency(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = formatRelativeTime(withdrawal.timestamp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = statusInfo.color.copy(alpha = 0.12f),
            ) {
                Text(
                    text = statusInfo.label,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusInfo.color,
                )
            }
        }
    }
}

