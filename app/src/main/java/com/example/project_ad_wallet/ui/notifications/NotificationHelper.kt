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
    private const val CHANNEL_ID = "new_ads_channel"

    fun showNewAdNotification(context: Context, adTitle: String) {
        // 1. Ler as preferências do utilizador
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val masterEnabled = prefs.getBoolean("master_enabled", true)
        val newAdsEnabled = prefs.getBoolean("new_ads_enabled", true)

        // Se o utilizador desligou as notificações, cancelamos aqui!
        if (!masterEnabled || !newAdsEnabled) return

        // 2. Criar o Canal de Notificações (Obrigatório para Android 8+)
        createNotificationChannel(context)

        // 3. Verificar permissões no Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return // Se não há permissão, não mostramos
            }
        }

        // 4. Construir e Mostrar a Notificação
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
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

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "New Advertisements"
            val descriptionText = "Notifications for newly available ads"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}