package com.example.project_ad_wallet.ui.withdrawals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.data.model.WithdrawalRequest
import com.example.project_ad_wallet.data.model.WithdrawalStatus
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.components.EmptyStateView
import com.example.project_ad_wallet.ui.components.asCurrency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WithdrawalStatusScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    viewModel: WithdrawalStatusViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdWalletTopBar(
                title = "Withdrawals",
                showBalance = false,
                showBack = true,
                onBack = { navController.popBackStack() },
            )
        },
        bottomBar = { AdWalletBottomBar(navController = navController, items = bottomNavItems) },
    ) { innerPadding ->
        if (uiState.isEmpty) {
            EmptyStateView(
                icon = Icons.Default.AccountBalance,
                message = "No withdrawal requests yet",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    WithdrawalSummaryCard(uiState)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Withdrawal History",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                items(uiState.withdrawals, key = { it.id }) { withdrawal ->
                    WithdrawalRequestItem(withdrawal = withdrawal)
                }
            }
        }
    }
}

@Composable
private fun WithdrawalSummaryCard(uiState: WithdrawalStatusUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("withdrawal_summary"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Total Withdrawn",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = uiState.totalWithdrawn.asCurrency(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            if (uiState.pendingCount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${uiState.pendingCount} request(s) pending",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun WithdrawalRequestItem(withdrawal: WithdrawalRequest) {
    val statusInfo = withdrawal.status.toDisplayInfo()
    val dateTimeFormatter = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    }
    val dateFormatter = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("withdrawal_item_${withdrawal.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = withdrawal.amount.asCurrency(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = dateTimeFormatter.format(Date(withdrawal.timestamp)),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (withdrawal.bankAccount.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Account ••••${withdrawal.bankAccount.takeLast(4)}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                StatusBadge(
                    label = statusInfo.label,
                    icon = statusInfo.icon,
                    color = statusInfo.color,
                )
            }

            // Show estimated completion date for pending/processing
            if (withdrawal.status == WithdrawalStatus.PENDING ||
                withdrawal.status == WithdrawalStatus.PROCESSING
            ) {
                withdrawal.estimatedCompletionDate?.let { estimatedDate ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estimated completion: ${dateFormatter.format(Date(estimatedDate))}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("withdrawal_estimated_${withdrawal.id}"),
                    )
                }
            }

            // Show rejection reason
            if (withdrawal.status == WithdrawalStatus.REJECTED && withdrawal.rejectionReason != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reason: ${withdrawal.rejectionReason}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("withdrawal_reason_${withdrawal.id}"),
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, icon: ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
            )
        }
    }
}
