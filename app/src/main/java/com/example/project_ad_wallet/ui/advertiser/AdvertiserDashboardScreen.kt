package com.example.project_ad_wallet.ui.advertiser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_ad_wallet.data.local.entities.AdStatsEntity
import com.example.project_ad_wallet.ui.components.AdWalletEmptyState

@Composable
fun AdvertiserDashboardScreen(
    onLogout: () -> Unit,
    viewModel: AdvertiserDashboardViewModel = hiltViewModel()
) {
    val adStats by viewModel.adStats.collectAsState()
    val totalViews by viewModel.totalViews.collectAsState()
    val totalCompletions by viewModel.totalCompletions.collectAsState()
    val completionRate by viewModel.completionRate.collectAsState()

    var activeTargetAd by remember { mutableStateOf<AdStatsEntity?>(null) }

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
                text = "Advertiser Dashboard",
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

        // Summary cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Total Views",
                value = totalViews.toString(),
                icon = Icons.Default.Visibility,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Completions",
                value = totalCompletions.toString(),
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Completion Rate",
                value = "${"%.1f".format(completionRate)}%",
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Os teus anúncios",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Lista de anúncios com estatísticas
        if (adStats.isEmpty()) {
            AdWalletEmptyState(
                icon = Icons.Default.BarChart,
                title = "Sem dados ainda",
                subtitle = "As estatísticas aparecem quando os utilizadores virem os teus anúncios."
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(adStats) { stat ->
                    AdStatCard(
                        stat = stat,
                        onTargetClick = { activeTargetAd = stat }
                    )
                }
            }
        }
    }

    activeTargetAd?.let { ad ->
        DemographicTargetDialog(
            adTitle = ad.adTitle,
            onDismiss = { activeTargetAd = null },
            onConfirm = { target ->
                viewModel.updateDemographicTarget(ad.adId, target)
                activeTargetAd = null
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF3FB950),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AdStatCard(
    stat: AdStatsEntity,
    onTargetClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stat.adTitle,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = onTargetClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF3FB950))
                ) {
                    Text("Targeting", fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatPill(text = "👁 ${stat.totalViews} views")
                StatPill(text = "✅ ${stat.totalCompletions} completions")
                StatPill(text = "⭐ ${"%.1f".format(stat.averageRating)}")
            }
        }
    }
}

@Composable
fun StatPill(text: String) {
    Text(text = text, color = Color.Gray, fontSize = 12.sp)
}
