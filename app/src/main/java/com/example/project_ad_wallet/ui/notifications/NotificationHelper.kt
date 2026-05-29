package com.example.project_ad_wallet.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    private const val NEW_ADS_CHANNEL_ID = "new_ads_channel"
    private const val EARNINGS_CHANNEL_ID = "earnings_channel"

    fun showNewAdNotification(context: Context, adTitle: String) {
        // 1. Ler as preferências do utilizador
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val masterEnabled = prefs.getBoolean("master_enabled", true)
        val newAdsEnabled = prefs.getBoolean("new_ads_enabled", true)

        // Se o utilizador desligou as notificações, cancelamos aqui!
        if (!masterEnabled || !newAdsEnabled) return

        // 2. Criar o Canal de Notificações
        createNotificationChannel(context, NEW_ADS_CHANNEL_ID, "New Advertisements", "Notifications for newly available ads")

        // 3. Verificar permissões no Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return // Se não há permissão, não mostramos
            }
        }

        // 4. Construir e Mostrar a Notificação
        val builder = NotificationCompat.Builder(context, NEW_ADS_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícone genérico do Android
            .setContentTitle("New Ad Available!")
            .setContentText("Earn rewards by watching: $adTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // O ID único é gerado a partir do nome do anúncio
            notify(adTitle.hashCode(), builder.build())
        }
    }

    fun showBalanceIncreaseNotification(context: Context, amount: Double) {
        // 1. Ler as preferências do utilizador
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val masterEnabled = prefs.getBoolean("master_enabled", true)
        val earningsEnabled = prefs.getBoolean("earnings_enabled", true)

        // Se o utilizador desligou as notificações de saldo ou gerais, cancelamos aqui!
        if (!masterEnabled || !earningsEnabled) return

        // 2. Criar o Canal de Notificações
        createNotificationChannel(context, EARNINGS_CHANNEL_ID, "Earnings Updates", "Notifications for earnings and balance updates")

        // 3. Verificar permissões no Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        // 4. Construir e Mostrar a Notificação
        val builder = NotificationCompat.Builder(context, EARNINGS_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Balance Increased!")
            .setContentText("You received a reward of $${"%.2f".format(amount)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // O ID único é gerado usando hash do montante e tempo atual
            notify((amount.hashCode() + System.currentTimeMillis().toInt()), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, channelDesc: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDesc
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}