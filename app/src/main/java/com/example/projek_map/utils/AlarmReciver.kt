package com.example.projek_map.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// 🔔 AlarmReceiver: ketika alarm ter-trigger, munculkan notifikasi.
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val type = intent?.getStringExtra("type") ?: "pengumuman"
        if (type == "jatuh_tempo") {
            NotificationHelper.showNotification(
                context,
                1001,
                "Pengingat Cicilan",
                "Ada cicilan yang menjelang jatuh tempo. Cek aplikasi koperasi sekarang."
            )
        } else {
            val title = intent?.getStringExtra("title") ?: "Pengumuman Koperasi"
            val msg = intent?.getStringExtra("message") ?: "Ada pengumuman baru dari koperasi."
            NotificationHelper.showNotification(context, 1002, title, msg)
        }
    }
}
