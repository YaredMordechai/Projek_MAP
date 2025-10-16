package com.example.projek_map.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projek_map.R
import com.example.projek_map.ui.MainActivity

object NotificationHelper {
    // ✅ Pertahankan konstanta lama
    const val CHANNEL_ID = "koperasi_channel_01"
    const val CHANNEL_NAME = "Pengumuman Koperasi"
    const val CHANNEL_DESC = "Notifikasi pengumuman dan pengingat jatuh tempo"

    // ====== Channel ======
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESC
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // ====== API lama (tetap ada) ======
    fun showNotification(context: Context, id: Int, title: String, message: String) {
        val builder = baseBuilder(context, title, message)
        notifyIfAllowed(context, id, builder)
    }

    // ====== API baru (dipakai AlarmReceiver) ======
    fun showDueNotification(context: Context, title: String, message: String) {
        val builder = baseBuilder(context, title, message)
        notifyIfAllowed(context, /*id*/ 1001, builder)
    }

    fun showAnnouncementNotification(context: Context, title: String, message: String) {
        val builder = baseBuilder(context, title, message)
        notifyIfAllowed(context, /*id*/ 2001, builder)
    }

    // ====== Util internal ======
    private fun baseBuilder(context: Context, title: String, message: String): NotificationCompat.Builder {
        // Tap notifikasi → buka MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            // bisa tambahkan extras jika perlu deep-link ke tab tertentu
        }
        val pendingIntent = androidx.core.app.PendingIntentCompat.getActivity(
            context,
            0,
            intent,
            0,
            false
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            // ⚠️ pastikan punya icon; jika tidak ada ic_notification, ganti ke ic_launcher_foreground
            .setSmallIcon(
                try {
                    R.drawable.ic_notification
                } catch (_: Exception) {
                    R.drawable.ic_launcher_foreground
                }
            )
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    private fun notifyIfAllowed(context: Context, id: Int, builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                notify(id, builder.build())
            } else {
                // izin belum diberikan (Android 13+), biarkan silent atau log
                android.util.Log.w("NotificationHelper", "POST_NOTIFICATIONS permission not granted")
            }
        }
    }
}
