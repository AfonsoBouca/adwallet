package com.example.project_ad_wallet.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Int,
    maxStars: Int = 5,
    onRatingSelected: (Int) -> Unit,
) {
    Row {
        for (star in 1..maxStars) {
            Icon(
                imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "Star $star",
                tint = if (star <= rating)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onRatingSelected(star) },
            )
        }
    }
}
