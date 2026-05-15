package com.example.project_ad_wallet.ui.advertiserUpload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.ConfirmationDialog
import com.example.project_ad_wallet.ui.registration.adWalletTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertiserUploadScreen(
    navController: NavHostController,
    viewModel: AdvertiserUploadViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var categoryExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            snackbarHostState.showSnackbar("Your ad has been submitted for review.")
            navController.popBackStack()
        }
    }

    if (uiState.showConfirmDialog) {
        ConfirmationDialog(
            title = "Submit for Review",
            message = "Submit this ad for review? It will appear in the feed once approved.",
            confirmLabel = "Submit",
            onConfirm = { viewModel.confirmUpload() },
            onDismiss = { viewModel.dismissDialog() },
        )
    }

    Scaffold(
        topBar = {
            AdWalletTopBar(
                showBalance = false,
                showBack = true,
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Upload Your Advertisement", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Fill in all fields to submit your ad for review.", color = MaterialTheme.colorScheme.onSurfaceVariant)

            FormField(
                label = "Ad Title",
                value = uiState.title,
                onValueChange = viewModel::onTitleChanged,
                error = uiState.titleError,
                testTag = "upload_title",
            )

            FormField(
                label = "Company Name",
                value = uiState.companyName,
                onValueChange = viewModel::onCompanyNameChanged,
                error = uiState.companyNameError,
                testTag = "upload_company",
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Category *", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                ) {
                    OutlinedTextField(
                        value = uiState.category,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("upload_category"),
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Select category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = adWalletTextFieldColors(),
                        isError = uiState.categoryError != null,
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                    ) {
                        AD_CATEGORIES.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { viewModel.onCategoryChanged(cat); categoryExpanded = false },
                            )
                        }
                    }
                }
                uiState.categoryError?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Description *", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_description"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Describe your ad (max 200 chars)") },
                    minLines = 3,
                    colors = adWalletTextFieldColors(),
                    isError = uiState.descriptionError != null,
                    supportingText = { Text("${uiState.description.length}/200") },
                )
                uiState.descriptionError?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
            }

            FormField(
                label = "Reward Amount (\$0.01 – \$1.00)",
                value = uiState.rewardAmount,
                onValueChange = viewModel::onRewardAmountChanged,
                error = uiState.rewardAmountError,
                keyboardType = KeyboardType.Decimal,
                testTag = "upload_reward",
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = THUMBNAIL_RES_IDS[uiState.selectedThumbnailIndex]),
                    contentDescription = "Thumbnail preview",
                    modifier = Modifier.size(80.dp),
                )
                OutlinedButton(
                    onClick = { viewModel.cycleThumbnail() },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                ) {
                    Text("Select Thumbnail", color = MaterialTheme.colorScheme.primary)
                }
            }

            OutlinedButton(
                onClick = { viewModel.cycleVideo() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            ) {
                Text("Select Video (${uiState.selectedVideoIndex + 1}/5)", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.submitAd() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("submit_ad_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
            ) {
                Text("SUBMIT FOR REVIEW", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            OutlinedButton(
                onClick = {
                    viewModel.cancelUpload()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text("CANCEL", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    testTag: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("$label *", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = adWalletTextFieldColors(),
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
    }
}
