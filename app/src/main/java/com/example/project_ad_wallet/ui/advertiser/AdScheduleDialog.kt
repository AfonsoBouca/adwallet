package com.example.project_ad_wallet.ui.advertiser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdScheduleDialog(
    adTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (startDate: String, endDate: String, dailyLimit: Int) -> Unit
) {
    var startDate by remember { mutableStateOf("2026-06-06") }
    var endDate by remember { mutableStateOf("2026-06-13") }
    var dailyLimit by remember { mutableStateOf(500) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF161B22),
        title = {
            Text(
                text = "Agendamento: $adTitle",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Configura a exibição e orçamento do anúncio.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                // Data de Início
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Data de Início (AAAA-MM-DD)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3FB950),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF3FB950),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Data de Fim
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Data de Fim (AAAA-MM-DD)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3FB950),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF3FB950),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Limite diário de visualizações
                Text(
                    text = "Limite diário de visualizações: $dailyLimit",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Slider(
                    value = dailyLimit.toFloat(),
                    onValueChange = { dailyLimit = it.toInt() },
                    valueRange = 100f..5000f,
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF3FB950),
                        activeTrackColor = Color(0xFF238636)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(startDate, endDate, dailyLimit)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF238636)
                )
            ) {
                Text("Confirmar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}
