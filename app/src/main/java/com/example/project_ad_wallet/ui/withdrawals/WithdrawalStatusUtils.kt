package com.example.project_ad_wallet.ui.withdrawals

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.project_ad_wallet.data.model.WithdrawalStatus

data class StatusDisplayInfo(
    val label: String,
    val icon: ImageVector,
    val color: Color,
)

fun WithdrawalStatus.toDisplayInfo(): StatusDisplayInfo {
    return when (this) {
        WithdrawalStatus.PENDING -> StatusDisplayInfo(
            label = "Pending",
            icon = Icons.Default.HourglassTop,
            color = Color(0xFFF59E0B),
        )
        WithdrawalStatus.PROCESSING -> StatusDisplayInfo(
            label = "Processing",
            icon = Icons.Default.Sync,
            color = Color(0xFF3B82F6),
        )
        WithdrawalStatus.COMPLETED -> StatusDisplayInfo(
            label = "Completed",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF22C55E),
        )
        WithdrawalStatus.REJECTED -> StatusDisplayInfo(
            label = "Rejected",
            icon = Icons.Default.Cancel,
            color = Color(0xFFEF4444),
        )
    }
}
