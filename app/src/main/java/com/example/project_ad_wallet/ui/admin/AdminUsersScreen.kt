package com.example.project_ad_wallet.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.ConfirmationDialog
import com.example.project_ad_wallet.ui.components.EmptyStateView

@Composable
fun AdminUsersScreen(
    users: List<User>,
    onToggleBlock: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado para controlar que utilizador foi clicado para abrir o Dialog
    var userToConfirmAction by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = {
            AdWalletTopBar(
                title = "Manage Users",
                showBalance = false, // Não faz sentido mostrar o saldo do admin aqui
                showBack = true,
                onBack = onBack
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (users.isEmpty()) {
            EmptyStateView(
                icon = Icons.Outlined.PeopleOutline,
                message = "No users found in the system.",
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(users) { user ->
                    AdminUserCard(
                        user = user,
                        onToggleClick = { userToConfirmAction = user }
                    )
                }
            }
        }
    }

    // Se o userToConfirmAction não for nulo, mostramos o ConfirmationDialog
    userToConfirmAction?.let { user ->
        val isBlocking = !user.isBlocked
        val actionText = if (isBlocking) "block" else "unblock"
        
        ConfirmationDialog(
            title = "${actionText.replaceFirstChar { it.uppercase() }} User",
            message = "Are you sure you want to $actionText ${user.name.ifEmpty { "this user" }}?",
            confirmLabel = "Yes, $actionText",
            dismissLabel = "Cancel",
            onConfirm = {
                onToggleBlock(user.id)
                userToConfirmAction = null
            },
            onDismiss = {
                userToConfirmAction = null
            }
        )
    }
}

@Composable
fun AdminUserCard(
    user: User,
    onToggleClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Mesmo shape do AdCard
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), // Mesmo border do AdCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name.ifEmpty { "Unknown User" },
                    fontSize = 18.sp, // Mesmo tamanho do título do AdCard
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Age: ${user.age} • Gender: ${user.gender.ifEmpty { "N/A" }}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Indicador visual de estado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (user.isBlocked) Icons.Default.Block else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (user.isBlocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (user.isBlocked) "Blocked" else "Active",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (user.isBlocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onToggleClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (user.isBlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(50) // Shape arredondado como os botões do Dialog
            ) {
                Text(
                    text = if (user.isBlocked) "Unblock" else "Block",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}