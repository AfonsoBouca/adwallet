package com.example.project_ad_wallet.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.example.project_ad_wallet.R

@Composable
fun AnimatedDonateButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.donate_button)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "button_scale"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .scale(scale)
            .semantics {
                contentDescription = text
            },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF238636))
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
