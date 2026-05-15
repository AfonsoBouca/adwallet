package com.example.project_ad_wallet.ui.adWatch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_ad_wallet.ui.registration.adWalletTextFieldColors

@Composable
fun AdReportDialog(
    adId: String,
    onDismiss: () -> Unit,
    viewModel: AdReportViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    AlertDialog(
        onDismissRequest = {
            if (uiState.isSubmitted) {
                viewModel.reset()
            }
            onDismiss()
        },
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = if (uiState.isSubmitted) "Report Submitted" else "Report Advertisement",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            if (uiState.isSubmitted) {
                Text(
                    text = "Thank you. We will review this ad shortly.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("report_submitted_message"),
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Why are you reporting this ad?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    REPORT_REASONS.forEach { reason ->
                        val isSelected = reason == uiState.selectedReason
                        if (isSelected) {
                            Button(
                                onClick = { viewModel.selectReason(reason) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reason_$reason"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White,
                                ),
                            ) { Text(reason) }
                        } else {
                            OutlinedButton(
                                onClick = { viewModel.selectReason(reason) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reason_$reason"),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) { Text(reason) }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.comment,
                        onValueChange = { viewModel.onCommentChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("report_comment_field"),
                        placeholder = { Text("Additional comments (optional)") },
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3,
                        colors = adWalletTextFieldColors(),
                    )
                }
            }
        },
        confirmButton = {
            if (uiState.isSubmitted) {
                Button(
                    onClick = {
                        viewModel.reset()
                        onDismiss()
                    },
                    shape = RoundedCornerShape(50),
                ) {
                    Text("OK")
                }
            } else {
                Button(
                    onClick = { viewModel.submitReport(adId) },
                    enabled = uiState.selectedReason != null,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .testTag("submit_report_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.38f),
                        disabledContentColor = Color.White.copy(alpha = 0.60f),
                    ),
                ) {
                    Text("Submit Report", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!uiState.isSubmitted) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
    )
}
