package com.example.project_ad_wallet.ui.adWatch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import kotlinx.coroutines.delay

@Composable
fun QuestionnaireScreen(
    adId: String,
    navController: NavHostController,
    viewModel: AdWatchViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val question = uiState.questions.getOrNull(uiState.currentQuestionIndex)

    LaunchedEffect(uiState.selectedAnswerIndex) {
        if (uiState.selectedAnswerIndex != null) {
            delay(1500)
            viewModel.advanceQuestion()
        }
    }

    Scaffold(
        topBar = {
            AdWalletTopBar(
                showBalance = false,
                showBack = true,
                onBack = { navController.popBackStack() },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Question ${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = question?.text ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))

            question?.options?.forEachIndexed { index, option ->
                val isSelected = uiState.selectedAnswerIndex == index
                val bgColor = when {
                    !isSelected && uiState.selectedAnswerIndex == null ->
                        MaterialTheme.colorScheme.surface
                    isSelected && uiState.isAnswerCorrect == true ->
                        Color(0xFF1A4731)
                    isSelected && uiState.isAnswerCorrect == false ->
                        Color(0xFF4A1A1A)
                    option.isCorrect && uiState.selectedAnswerIndex != null ->
                        Color(0xFF1A4731)
                    else ->
                        MaterialTheme.colorScheme.surface
                }
                val borderColor = when {
                    isSelected && uiState.isAnswerCorrect == true -> Color(0xFF4ADE80)
                    isSelected && uiState.isAnswerCorrect == false -> Color(0xFFF87171)
                    option.isCorrect && uiState.selectedAnswerIndex != null -> Color(0xFF4ADE80)
                    else -> MaterialTheme.colorScheme.outline
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                        .background(bgColor, RoundedCornerShape(12.dp))
                        .clickable(enabled = uiState.selectedAnswerIndex == null) {
                            viewModel.selectAnswer(index)
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = option.text,
                        color = if (isSelected || (option.isCorrect && uiState.selectedAnswerIndex != null))
                            Color.White
                        else
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
