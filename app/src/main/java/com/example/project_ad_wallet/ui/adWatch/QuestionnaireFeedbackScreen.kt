package com.example.project_ad_wallet.ui.adWatch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_ad_wallet.data.model.QuestionResult
import com.example.project_ad_wallet.ui.components.AdWalletTopBar

@Composable
fun QuestionnaireFeedbackScreen(
    correctCount: Int,
    totalQuestions: Int,
    questionResults: List<QuestionResult>,
    onContinue: () -> Unit,
) {
    val scorePercent = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
    val allCorrect = correctCount == totalQuestions

    Scaffold(
        topBar = {
            AdWalletTopBar(
                showBalance = false,
                showBack = false,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Score icon
            Icon(
                imageVector = if (allCorrect) Icons.Default.CheckCircle else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (allCorrect) Color(0xFF4ADE80) else Color(0xFFFBBF24),
                modifier = Modifier.size(64.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (allCorrect) "Perfect Score!" else "Quiz Complete!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("feedback_title"),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You got $correctCount out of $totalQuestions correct ($scorePercent%)",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("feedback_score"),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Per-question results
            questionResults.forEachIndexed { index, result ->
                QuestionResultCard(
                    questionNumber = index + 1,
                    result = result,
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("feedback_continue_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = "COLLECT REWARD",
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuestionResultCard(
    questionNumber: Int,
    result: QuestionResult,
) {
    val borderColor = if (result.isCorrect) Color(0xFF4ADE80) else Color(0xFFF87171)
    val bgColor = if (result.isCorrect) Color(0xFF1A4731) else Color(0xFF4A1A1A)
    val iconColor = if (result.isCorrect) Color(0xFF4ADE80) else Color(0xFFF87171)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .background(bgColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (result.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = if (result.isCorrect) "Correct" else "Incorrect",
                tint = iconColor,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Question $questionNumber",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f),
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = result.questionText,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.7f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your answer: ${result.selectedAnswer}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (result.isCorrect) Color(0xFF4ADE80) else Color(0xFFF87171),
        )

        if (!result.isCorrect) {
            Text(
                text = "Correct answer: ${result.correctAnswer}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4ADE80),
            )
        }
    }
}
