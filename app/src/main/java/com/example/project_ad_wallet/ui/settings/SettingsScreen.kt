package com.example.project_ad_wallet.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.model.User
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.components.ConfirmationDialog
import com.example.project_ad_wallet.ui.navigation.Routes
import com.example.project_ad_wallet.ui.registration.EditProfileDialog

import com.example.project_ad_wallet.ui.components.AdWalletEmptyState
import com.example.project_ad_wallet.ui.components.AdWalletErrorState
import com.example.project_ad_wallet.ui.components.AdWalletLoadingState
import androidx.compose.foundation.layout.Box

import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.project_ad_wallet.ui.profile.ProfilePictureViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.material3.IconButton

@Composable
fun SettingsScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onResetApp: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    profileViewModel: ProfilePictureViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by profileViewModel.userProfile.collectAsState()
    var showEditProfile by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.updateProfilePicture(it) }
    }

    Scaffold(
        topBar = { AdWalletTopBar(title = "Settings", showBalance = false) },
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        ProfileSection(
                            user = uiState.user,
                            onEdit = { showEditProfile = true },
                            profilePictureUri = userProfile?.profilePictureUri,
                            onPickImage = { launcher.launch("image/*") }
                        )

                        NotificationsSection(navController = navController)

                        ThemeSection(
                            currentMode = uiState.themeMode,
                            onModeChanged = {
                                viewModel.setThemeMode(it)
                                onThemeModeChanged(it)
                            },
                        )

                        AccountSection(
                            onDeleteAccount = { showDeleteConfirm = true },
                        )

                        DeveloperSection(
                            onResetApp = {
                                viewModel.resetApp()
                                onResetApp()
                            },
                            onAddFunds = viewModel::onAddFunds,
                            onGenerateRandomHistory = viewModel::onGenerateRandomHistory,
                            onLogout = {
                                viewModel.logout()
                                onLogout()
                            },
                        )
                    }
                }
            }
        }
    }

    if (showEditProfile) {
        EditProfileDialog(
            initialUser = uiState.user,
            onDismiss = { showEditProfile = false },
            onSave = { name, age, gender ->
                viewModel.updateProfile(name, age, gender)
                showEditProfile = false
            },
        )
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = "Delete Account",
            message = "Are you sure you want to permanently delete your account? All your data, earnings, and history will be removed. This action cannot be undone.",
            confirmLabel = "Delete",
            dismissLabel = "Cancel",
            onConfirm = {
                showDeleteConfirm = false
                viewModel.deleteAccount()
                onDeleteAccount()
            },
            onDismiss = { showDeleteConfirm = false },
        )
    }
}

@Composable
private fun ProfileSection(
    user: User,
    onEdit: () -> Unit,
    profilePictureUri: String?,
    onPickImage: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(icon = Icons.Default.Person, title = "Profile", actionLabel = "Edit", onActionClick = onEdit)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfilePictureSection(
                profilePictureUri = profilePictureUri,
                onPickImage = onPickImage
            )
            
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ProfileItem("Name", user.name)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileItem("Age", user.age.toString())
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileItem("Gender", user.gender)
                }
            }
        }
    }
}

@Composable
fun ProfilePictureSection(
    profilePictureUri: String?,
    onPickImage: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        // Foto ou placeholder
        if (profilePictureUri != null) {
            AsyncImage(
                model = profilePictureUri,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF238636), CircleShape)
                    .clickable { onPickImage() },
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF161B22))
                    .border(2.dp, Color(0xFF238636), CircleShape)
                    .clickable { onPickImage() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF3FB950),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Botão de editar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(
                    onClick = onPickImage,
                    onClickLabel = "Alterar foto"
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF238636)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Alterar foto",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationsSection(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(icon = Icons.Default.Notifications, title = "Notifications")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Routes.NOTIFICATION_PREFS) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Notification Preferences",
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = ">",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ThemeSection(currentMode: ThemeMode, onModeChanged: (ThemeMode) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(icon = Icons.Default.Settings, title = "Appearance")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ThemeModeRow("System default", currentMode == ThemeMode.System) { onModeChanged(ThemeMode.System) }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                ThemeModeRow("Light mode", currentMode == ThemeMode.Light) { onModeChanged(ThemeMode.Light) }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                ThemeModeRow("Dark mode", currentMode == ThemeMode.Dark) { onModeChanged(ThemeMode.Dark) }
            }
        }
    }
}

@Composable
private fun AccountSection(
    onDeleteAccount: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(icon = Icons.Default.Person, title = "Account")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Permanently delete your account and all associated data.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDeleteAccount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("delete_account_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DELETE ACCOUNT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DeveloperSection(
    onResetApp: () -> Unit,
    onAddFunds: (Double) -> Unit,
    onGenerateRandomHistory: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(icon = Icons.Default.Settings, title = "Developer Options")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Wallet Management", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val modifier = Modifier.weight(1f)
                    OutlinedButton(onClick = { onAddFunds(10.0) }, modifier = modifier, contentPadding = PaddingValues(0.dp)) { Text("+$10") }
                    OutlinedButton(onClick = { onAddFunds(50.0) }, modifier = modifier, contentPadding = PaddingValues(0.dp)) { Text("+$50") }
                    OutlinedButton(onClick = { onAddFunds(100.0) }, modifier = modifier, contentPadding = PaddingValues(0.dp)) { Text("+$100") }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                
                Text("Data Generation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Button(
                    onClick = onGenerateRandomHistory,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("GENERATE RANDOM HISTORY")
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                OutlinedButton(
                    onClick = onResetApp,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text("RESET ALL APP DATA", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("logout_button"),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                ) {
                    Text("LOG OUT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.weight(1f))
        if (actionLabel != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(actionLabel, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ProfileItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value.ifBlank { "Not set" },
            color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ThemeModeRow(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.weight(1f))
        if (selected) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
    }
}
