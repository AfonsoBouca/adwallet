package com.example.project_ad_wallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.domain.StreakCalculator

import androidx.compose.ui.platform.testTag

@Composable
fun StreakWidget(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("streak_widget"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔥",
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "$streakDays dias",
                    color = Color(0xFF3FB950),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("streak_days_text")
                )
                Text(
                    text = streakLabel(streakDays),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            if (StreakCalculator.getStreakBonus(streakDays) > 0) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "+${(StreakCalculator.getStreakBonus(streakDays) * 100).toInt()}%",
                    color = Color(0xFF3FB950),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("streak_bonus_text")
                )
            }
        }
    }
}

private fun streakLabel(days: Int): String = when {
    days >= 30 -> "Lenda! Bónus de 50%"
    days >= 14 -> "Incrível! Bónus de 30%"
    days >= 7  -> "Uma semana! Bónus de 20%"
    days >= 3  -> "A aquecer! Bónus de 10%"
    days == 1  -> "Primeiro dia!"
    else       -> "Começa hoje!"
}
