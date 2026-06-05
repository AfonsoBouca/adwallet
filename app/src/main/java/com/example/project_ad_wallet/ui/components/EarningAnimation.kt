package com.example.project_ad_wallet.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun EarningAnimation(amount: Double, visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Text(
            text = "+€${"%.2f".format(amount)}",
            color = Color(0xFF3FB950),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
