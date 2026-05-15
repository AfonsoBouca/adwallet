package com.example.project_ad_wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_ad_wallet.ui.navigation.AdWalletNavGraph
import com.example.project_ad_wallet.data.repository.UserRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userRepository = UserRepository.instance
            val themeMode by userRepository.themeMode.collectAsState()
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.System -> systemDark
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }
            
            Project_Ad_WalletTheme(darkTheme = darkTheme) {
                AdWalletNavGraph(
                    onThemeModeChanged = { userRepository.setThemeMode(it) }
                )
            }
        }
    }
}
