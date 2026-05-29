package com.example.project_ad_wallet

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.ui.navigation.AdWalletNavGraph
import com.example.project_ad_wallet.ui.theme.Project_Ad_WalletTheme

class MainActivity : ComponentActivity() {

    // Registar a chamada do sistema para pedir a permissão
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Se quiséssemos reagir à recusa, fazíamos aqui, mas o sistema já trata do bloqueio
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Pedir o pop-up de permissão de notificações se o telemóvel for Android 13 ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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