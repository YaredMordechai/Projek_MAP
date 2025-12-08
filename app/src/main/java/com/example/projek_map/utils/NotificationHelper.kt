package com.example.projek_map.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projek_map.R
import com.example.projek_map.ui.MainActivity

object NotificationHelper {
    const val CHANNEL_ID = "koperasi_channel_01"
    const val CHANNEL_NAME = "Pengumuman Koperasi"
    const val CHANNEL_DESC = "Notifikasi pengumuman dan pengingat jatuh tempo"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = CHANNEL_DESC }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // ========= API umum =========
    fun showNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        dest: String? = null // ex: "pengumuman", "pinjaman"
    ) {
        val builder = baseBuilder(context, title, message, dest)
        notifyIfAllowed(context, id, builder)
    }

    // ========= API khusus =========
    fun showDueNotification(context: Context, title: String, message: String) {
        val builder = baseBuilder(context, title, message, dest = "jatuh_tempo")
        notifyIfAllowed(context, /*fixed id to overwrite*/ 1001, builder)
    }

    fun showAnnouncementNotification(context: Context, title: String, message: String) {
        val builder = baseBuilder(context, title, message, dest = "pengumuman")
        notifyIfAllowed(context, /*fixed id to overwrite*/ 2001, builder)
    }

    // ========= Util internal =========
    private fun baseBuilder(
        context: Context,
        title: String,
        message: String,
        dest: String?
    ): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            // Biar MainActivity bisa langsung buka tab tertentu
            dest?.let { putExtra("dest", it) }
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val flags = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0) or PendingIntent.FLAG_UPDATE_CURRENT

        val pendingIntent = PendingIntent.getActivity(
            context,
            /*requestCode*/ dest.hashCode(), // beda dest → beda PendingIntent
            intent,
            flags
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // pastikan icon ada
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setGroup("koperasi_notif") // opsional: pengelompokan
    }

    private fun notifyIfAllowed(
        context: Context,
        id: Int,
        builder: NotificationCompat.Builder
    ) {
        val canNotify = if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true // < Android 13 tidak butuh runtime permission
        }

        if (canNotify) {
            NotificationManagerCompat.from(context).notify(id, builder.build())
        } else {
            android.util.Log.w("NotificationHelper", "POST_NOTIFICATIONS not granted on API 33+")
        }
    }
}
