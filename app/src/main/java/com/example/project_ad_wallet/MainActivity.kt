package com.example.project_ad_wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_ad_wallet.domain.model.ThemeMode
import com.example.project_ad_wallet.ui.screen.AdWalletApp
import com.example.project_ad_wallet.ui.theme.Project_Ad_WalletTheme
import com.example.project_ad_wallet.ui.viewmodel.WalletViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: WalletViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            val systemDarkTheme = isSystemInDarkTheme()
            val darkTheme = when (uiState.settings.themeMode) {
                ThemeMode.System -> systemDarkTheme
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }

            Project_Ad_WalletTheme(darkTheme = darkTheme) {
                AdWalletApp(
                    viewModel = viewModel,
                    uiState = uiState,
                )
            }
        }
    }
}
