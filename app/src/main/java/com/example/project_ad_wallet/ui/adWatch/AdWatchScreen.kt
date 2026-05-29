package com.example.project_ad_wallet.ui.adWatch

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.project_ad_wallet.ui.components.AdWalletTopBar
import com.example.project_ad_wallet.ui.components.asCurrency
import com.example.project_ad_wallet.ui.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun AdWatchScreen(
    adId: String,
    navController: NavHostController,
    viewModel: AdWatchViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showReportDialog by remember { mutableStateOf(false) }

    LaunchedEffect(adId) {
        viewModel.loadAd(adId)
    }

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            navController.navigate(Routes.adRating(adId)) {
                popUpTo(Routes.adWatch(adId)) { inclusive = true }
            }
        }
    }

    if (uiState.rewardGranted && !uiState.isComplete) {
        RewardOverlay(
            rewardAmount = uiState.rewardAmount,
            onDismiss = { viewModel.dismissReward() },
        )
        return
    }

    if (uiState.showQuestionnaire) {
        QuestionnaireScreen(
            adId = adId,
            navController = navController,
            viewModel = viewModel,
        )
        return
    }

    val ad = uiState.ad ?: return

    Scaffold(
        topBar = {
            AdWalletTopBar(
                showBalance = false,
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(
                        onClick = { showReportDialog = true },
                        modifier = Modifier.testTag("report_ad_button"),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Report ad",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = ad.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("ad_watch_title"),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ad.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))

            VideoPlayer(
                videoResId = ad.videoResId,
                onProgressChanged = { viewModel.updateProgress(it) },
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { uiState.watchProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Watched: ${(uiState.watchProgress * 100).toInt()}%",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.proceedToQuestionnaire() },
                enabled = uiState.isSkipEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("skip_to_questions_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledContentColor = Color.White.copy(alpha = 0.60f),
                ),
            ) {
                Text(
                    text = if (uiState.isSkipEnabled) "SKIP TO QUESTIONS" else "Watch 80% to unlock",
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.skipAd()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("skip_not_relevant_button"),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("SKIP AD (NOT RELEVANT)", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showReportDialog) {
        AdReportDialog(
            adId = adId,
            onDismiss = { showReportDialog = false },
        )
    }
}

@Composable
private fun VideoPlayer(
    videoResId: Int,
    onProgressChanged: (Float) -> Unit,
) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }

    DisposableEffect(videoResId) {
        val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.start()
        onDispose {
            videoView.stopPlayback()
        }
    }

    LaunchedEffect(videoView) {
        while (true) {
            delay(500)
            val duration = videoView.duration.takeIf { it > 0 } ?: continue
            val position = videoView.currentPosition
            val progress = (position.toFloat() / duration).coerceIn(0f, 1f)
            onProgressChanged(progress)
            if (!videoView.isPlaying && position >= duration) {
                onProgressChanged(1f)
                break
            }
        }
    }

    AndroidView(
        factory = { 
            videoView.apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Black, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .aspectRatio(16/9f),
    )
}

@Composable
private fun RewardOverlay(
    rewardAmount: Double,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = Color(0xFF4ADE80),
                modifier = Modifier.size(80.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You earned ${rewardAmount.asCurrency()}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("reward_text"),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Reward added to your wallet",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
            )
        }
    }
}
