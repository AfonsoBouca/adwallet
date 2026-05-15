package com.example.project_ad_wallet.ui.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem

@Composable
fun NotificationPrefsScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    viewModel: NotificationPrefsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AdWalletTopBar(
                showBalance = false,
                showBack = true,
                onBack = { navController.popBackStack() },
            )
        },
        bottomBar = { AdWalletBottomBar(navController = navController, items = bottomNavItems) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "Notification Preferences",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Choose which notifications you'd like to receive.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    NotifToggleRow(
                        label = "New Ads Available",
                        description = "Get notified when new ads are ready to watch",
                        checked = uiState.newAdsEnabled,
                        onCheckedChange = viewModel::setNewAdsEnabled,
                        testTag = "toggle_new_ads",
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifToggleRow(
                        label = "Earnings Updates",
                        description = "Know when your balance changes",
                        checked = uiState.earningsEnabled,
                        onCheckedChange = viewModel::setEarningsEnabled,
                        testTag = "toggle_earnings",
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifToggleRow(
                        label = "Donation Reminders",
                        description = "Reminders to donate your balance to charity",
                        checked = uiState.donationRemindersEnabled,
                        onCheckedChange = viewModel::setDonationRemindersEnabled,
                        testTag = "toggle_donation_reminders",
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifToggleRow(
                        label = "Weekly Digest",
                        description = "A weekly summary of your activity",
                        checked = uiState.weeklyDigestEnabled,
                        onCheckedChange = viewModel::setWeeklyDigestEnabled,
                        testTag = "toggle_weekly_digest",
                    )
                }
            }
        }
    }
}

@Composable
private fun NotifToggleRow(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
        )
    }
}
