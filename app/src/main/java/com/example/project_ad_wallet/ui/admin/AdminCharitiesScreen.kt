package com.example.project_ad_wallet.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.data.model.Charity
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.ConfirmationDialog
import com.example.project_ad_wallet.ui.components.EmptyStateView

@Composable
fun AdminCharitiesScreen(
    pendingCharities: List<Charity>,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var charityToConfirmAction by remember { mutableStateOf<Pair<Charity, Boolean>?>(null) } // Pair(Charity, isApprove)

    Scaffold(
        topBar = {
            AdWalletTopBar(
                title = "Verify Charities",
                showBalance = false,
                showBack = true,
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (pendingCharities.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.VolunteerActivism,
                message = "No pending charities to review at the moment.",
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
                items(pendingCharities, key = { it.id }) { charity ->
                    AdminCharityCard(
                        charity = charity,
                        onApproveClick = { charityToConfirmAction = Pair(charity, true) },
                        onRejectClick = { charityToConfirmAction = Pair(charity, false) }
                    )
                }
            }
        }
    }

    charityToConfirmAction?.let { (charity, isApprove) ->
        val actionText = if (isApprove) "verify" else "reject"
        
        ConfirmationDialog(
            title = "${actionText.replaceFirstChar { it.uppercase() }} Organization",
            message = "Are you sure you want to $actionText '${charity.name}'?",
            confirmLabel = "Yes, $actionText",
            dismissLabel = "Cancel",
            onConfirm = {
                if (isApprove) onApprove(charity.id) else onReject(charity.id)
                charityToConfirmAction = null
            },
            onDismiss = {
                charityToConfirmAction = null
            }
        )
    }
}

@Composable
fun AdminCharityCard(
    charity: Charity,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = charity.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "SDG ${charity.sdgNumber} • ${charity.sdgName}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = charity.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

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
                    Text("Verify", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}