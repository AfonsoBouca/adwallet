package com.example.project_ad_wallet.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        icon = Icons.Default.PlayCircle,
        title = "Vê anúncios, ganha saldo",
        description = "Dedica alguns minutos a ver anúncios relevantes e acumula saldo na tua carteira virtual."
    ),
    OnboardingPage(
        icon = Icons.Default.Favorite,
        title = "Doa a quem precisas",
        description = "Usa o teu saldo para fazer doações a ONGs alinhadas com os Objetivos de Desenvolvimento Sustentável da ONU."
    ),
    OnboardingPage(
        icon = Icons.Default.EmojiEvents,
        title = "Mantém a tua streak",
        description = "Vê anúncios todos os dias para manter a tua sequência e desbloquear recompensas especiais."
    )
)
