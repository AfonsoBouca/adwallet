package com.example.project_ad_wallet.ui.advertiser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.data.model.DemographicTarget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemographicTargetDialog(
    adTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (DemographicTarget) -> Unit
) {
    var minAge by remember { mutableStateOf(18) }
    var maxAge by remember { mutableStateOf(65) }
    val genderOptions = listOf("Male", "Female", "Other")
    val selectedGenders = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF161B22),
        title = {
            Text(
                text = "Targeting: $adTitle",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Idade mínima
                Text("Idade mínima: $minAge", color = Color.White)
                Slider(
                    value = minAge.toFloat(),
                    onValueChange = { minAge = it.toInt() },
                    valueRange = 13f..80f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF3FB950),
                        activeTrackColor = Color(0xFF238636)
                    )
                )

                // Idade máxima
                Text("Idade máxima: $maxAge", color = Color.White)
                Slider(
                    value = maxAge.toFloat(),
                    onValueChange = { maxAge = it.toInt() },
                    valueRange = 13f..80f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF3FB950),
                        activeTrackColor = Color(0xFF238636)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Géneros
                Text(
                    text = "Género (vazio = todos)",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    genderOptions.forEach { gender ->
                        val selected = selectedGenders.contains(gender)
                        FilterChip(
                            selected = selected,
                            onClick = {
                                if (selected) selectedGenders.remove(gender)
                                else selectedGenders.add(gender)
                            },
                            label = { Text(gender, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF238636),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        DemographicTarget(
                            minAge = minAge,
                            maxAge = maxAge,
                            targetGenders = selectedGenders.toList()
                        )
                    )
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
