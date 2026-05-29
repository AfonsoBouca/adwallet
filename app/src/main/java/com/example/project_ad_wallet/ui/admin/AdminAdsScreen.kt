package com.example.project_ad_wallet.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.data.model.Ad
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.ConfirmationDialog
import com.example.project_ad_wallet.ui.components.EmptyStateView
import com.example.project_ad_wallet.ui.components.asCurrency
import com.example.project_ad_wallet.ui.notifications.NotificationHelper

@Composable
fun AdminAdsScreen(
    pendingAds: List<Ad>,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var adToConfirmAction by remember { mutableStateOf<Pair<Ad, Boolean>?>(null) } // Pair(Ad, isApprove)
    val context = LocalContext.current // <-- Apanhamos o contexto para as notificações

    Scaffold(
        topBar = {
            AdWalletTopBar(
                title = "Review Ads",
                showBalance = false,
                showBack = true,
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (pendingAds.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.Campaign,
                message = "No pending ads to review at the moment.",
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pendingAds, key = { it.id }) { ad ->
                    AdminAdCard(
                        ad = ad,
                        onApproveClick = { adToConfirmAction = Pair(ad, true) },
                        onRejectClick = { adToConfirmAction = Pair(ad, false) }
                    )
                }
            }
        }
    }

    adToConfirmAction?.let { (ad, isApprove) ->
        val actionText = if (isApprove) "approve" else "reject"
        
        ConfirmationDialog(
            title = "${actionText.replaceFirstChar { it.uppercase() }} Ad",
            message = "Are you sure you want to $actionText '${ad.title}'?",
            confirmLabel = "Yes, $actionText",
            dismissLabel = "Cancel",
            onConfirm = {
                if (isApprove) {
                    onApprove(ad.id)
                    // <-- DISPARO DA NOTIFICAÇÃO AQUI!
                    NotificationHelper.showNewAdNotification(context, ad.title)
                } else {
                    onReject(ad.id)
                }
                adToConfirmAction = null
            },
            onDismiss = {
                adToConfirmAction = null
            }
        )
    }
}

@Composable
fun AdminAdCard(
    ad: Ad,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header do Ad (Título e Recompensa)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ad.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${ad.advertiserName} • ${ad.category}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ad.rewardAmount.asCurrency(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Descrição
            Text(
                text = ad.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Botões de Ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onApproveClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}