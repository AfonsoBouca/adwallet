package com.example.project_ad_wallet.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_ad_wallet.data.local.entities.UserActivityEntity
import com.example.project_ad_wallet.ui.components.AdWalletEmptyState

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val recentActivity by viewModel.recentActivity.collectAsState()
    val flaggedActivity by viewModel.flaggedActivity.collectAsState()
    val flaggedCount by viewModel.flaggedCount.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Atividade Recente", "Suspeitos ($flaggedCount)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Admin Dashboard",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.Gray
                )
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF161B22),
            contentColor = Color(0xFF3FB950)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontSize = 13.sp) }
                )
            }
        }

        // Conteúdo das tabs
        when (selectedTab) {
            0 -> ActivityList(
                activities = recentActivity,
                onFlag = { id -> viewModel.flagActivity(id, "Flagged manually") },
                emptyMessage = "Sem atividade recente"
            )
            1 -> ActivityList(
                activities = flaggedActivity,
                onFlag = null,
                emptyMessage = "Sem atividade suspeita"
            )
        }
    }
}

@Composable
fun ActivityList(
    activities: List<UserActivityEntity>,
    onFlag: ((Long) -> Unit)?,
    emptyMessage: String
) {
    if (activities.isEmpty()) {
        AdWalletEmptyState(
            icon = Icons.Default.Shield,
            title = emptyMessage,
            subtitle = ""
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activities) { activity ->
                ActivityItem(activity = activity, onFlag = onFlag)
            }
        }
    }
}

@Composable
fun ActivityItem(
    activity: UserActivityEntity,
    onFlag: ((Long) -> Unit)?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (activity.isFlagged) Color(0xFF2D1B1B) else Color(0xFF161B22)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (activity.isFlagged) {
                        Text("🚨 ", fontSize = 12.sp)
                    }
                    Text(
                        text = activity.userName,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = activity.activityType,
                    color = Color(0xFF3FB950),
                    fontSize = 12.sp
                )
                if (activity.details.isNotEmpty()) {
                    Text(text = activity.details, color = Color.Gray, fontSize = 11.sp)
                }
                if (activity.flagReason != null) {
                    Text("⚠️ ${activity.flagReason}", color = Color.Red, fontSize = 11.sp)
                }
            }
            if (onFlag != null && !activity.isFlagged) {
                IconButton(onClick = { onFlag(activity.id) }) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Flag",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
