package com.example.project_ad_wallet

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.project_ad_wallet.ui.notifications.NotificationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class NotificationHelperTest {

    private lateinit var context: Application

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        // Reset preferences before each test
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    @Test
    fun test_showBalanceIncreaseNotification_whenEnabled() {
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("master_enabled", true)
            .putBoolean("earnings_enabled", true)
            .apply()

        NotificationHelper.showBalanceIncreaseNotification(context, 15.50)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = shadowOf(notificationManager).allNotifications
        
        assertEquals(1, notifications.size)
        val notification = notifications[0]
        
        // Verify notification fields
        val extras = notification.extras
        assertEquals("Balance Increased!", extras.getCharSequence("android.title"))
        assertEquals("You received a reward of $15.50", extras.getCharSequence("android.text"))
    }

    @Test
    fun test_showBalanceIncreaseNotification_whenMasterDisabled() {
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("master_enabled", false)
            .putBoolean("earnings_enabled", true)
            .apply()

        NotificationHelper.showBalanceIncreaseNotification(context, 15.50)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = shadowOf(notificationManager).allNotifications
        
        assertTrue(notifications.isEmpty())
    }

    @Test
    fun test_showBalanceIncreaseNotification_whenEarningsDisabled() {
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("master_enabled", true)
            .putBoolean("earnings_enabled", false)
            .apply()

        NotificationHelper.showBalanceIncreaseNotification(context, 15.50)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = shadowOf(notificationManager).allNotifications
        
        assertTrue(notifications.isEmpty())
    }
}
