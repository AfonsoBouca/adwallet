package com.example.project_ad_wallet.ui.charities

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import com.example.project_ad_wallet.ui.navigation.Routes
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.ui.components.AdWalletBottomBar
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.asCurrency
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.registration.adWalletTextFieldColors

import com.example.project_ad_wallet.ui.components.AdWalletEmptyState
import com.example.project_ad_wallet.ui.components.AdWalletErrorState
import com.example.project_ad_wallet.ui.components.AdWalletLoadingState
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.foundation.layout.Box

@Composable
fun CharitiesScreen(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    preselectedCharityId: String? = null,
    viewModel: CharitiesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val charityOfWeekViewModel: com.example.project_ad_wallet.ui.charities.CharityOfWeekViewModel = hiltViewModel()
    val charityOfWeek by charityOfWeekViewModel.charityOfWeek.collectAsState()

    LaunchedEffect(uiState.charities, preselectedCharityId) {
        if (!preselectedCharityId.isNullOrBlank() && uiState.charities.isNotEmpty()) {
            val charity = uiState.charities.find { it.id == preselectedCharityId }
            if (charity != null) {
                viewModel.selectCharity(charity)
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        val msg = uiState.errorMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.confirmedDonation) {
        val (charityId, amount) = uiState.confirmedDonation ?: return@LaunchedEffect
        viewModel.clearConfirmedDonation()
        navController.navigate(Routes.donationConfirmation(charityId, amount))
    }

    Scaffold(
        topBar = {
            AdWalletTopBar(title = "Charities", balance = uiState.balance)
        },
        bottomBar = { AdWalletBottomBar(navController = navController, items = bottomNavItems) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                uiState.charities.isEmpty() -> {
                    AdWalletEmptyState(
                        icon = Icons.Default.VolunteerActivism,
                        title = "Sem instituições",
                        subtitle = "Não há instituições verificadas de momento."
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        if (uiState.userRole == "USER" && charityOfWeek != null) {
                            item {
                                com.example.project_ad_wallet.ui.components.CharityOfWeekCard(
                                    charity = charityOfWeek!!,
                                    onDonateClick = {
                                        val c = charityOfWeek!!
                                        viewModel.selectCharity(
                                            com.example.project_ad_wallet.data.model.Charity(
                                                id = c.charityId,
                                                name = c.charityName,
                                                description = c.charityDescription,
                                                sdgNumber = c.sdgNumber,
                                                sdgName = c.sdgDescription,
                                                status = com.example.project_ad_wallet.data.model.CharityStatus.VERIFIED
                                            )
                                        )
                                    }
                                )
                            }
                        }
                        items(uiState.charities, key = { it.id }) { charity ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectCharity(charity) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = charity.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = charity.description,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                                        ) {
                                            Text(
                                                text = "SDG ${charity.sdgNumber} — ${charity.sdgName}",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                        }
                                        val totalDonated = uiState.charityDonations[charity.id] ?: 0.0
                                        Text(
                                            text = "Total Donated: ${totalDonated.asCurrency()}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.testTag("charity_total_donated_${charity.id}"),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    uiState.selectedCharity?.let { charity ->
        val parsedAmount = uiState.donationAmount.toDoubleOrNull()
        val confirmEnabled = parsedAmount != null && parsedAmount > 0.0 && parsedAmount <= uiState.balance

        AlertDialog(
            onDismissRequest = { viewModel.dismissCharity() },
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Donate to ${charity.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "How much would you like to donate?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = uiState.donationAmount,
                        onValueChange = { input ->
                            viewModel.onAmountChanged(
                                input.filterIndexed { index, char ->
                                    char.isDigit() || (char == '.' && '.' !in input.take(index))
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("0.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = adWalletTextFieldColors(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf("$1" to "1.00", "$5" to "5.00").forEach { (label, value) ->
                            OutlinedButton(
                                onClick = { viewModel.onAmountChanged(value) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary,
                                ),
                            ) { Text(label) }
                        }
                        OutlinedButton(
                            onClick = { viewModel.onAmountChanged("%.2f".format(uiState.balance)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) { Text("Max") }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDonation() },
                    enabled = confirmEnabled,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                        disabledContentColor = Color.White.copy(alpha = 0.60f),
                    ),
                ) {
                    Text("Confirm Donation", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissCharity() }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
        )
    }
}
