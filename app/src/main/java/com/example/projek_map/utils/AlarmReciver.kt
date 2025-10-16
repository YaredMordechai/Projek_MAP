package com.example.projek_map.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.projek_map.data.DummyUserData
import java.text.SimpleDateFormat
import java.util.Locale

// 🔔 AlarmReceiver gabungan: dukung 2 mode sekaligus
// - Mode A: intent extra "type" = "jatuh_tempo" / "pengumuman"  -> kirim notifikasi sesuai extra
// - Mode B: TANPA extra "type" -> cek auto: due cicilan & pengumuman baru dari DummyUserData
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val type = intent?.getStringExtra("type")

        if (type != null) {
            // =========================
            // Mode A: via intent extras
            // =========================
            when (type) {
                "jatuh_tempo" -> {
                    NotificationHelper.showNotification(
                        context = context,
                        id = 1001,
                        title = "Pengingat Cicilan",
                        message = "Ada cicilan yang menjelang jatuh tempo. Cek aplikasi koperasi sekarang."
                    )
                }
                else -> { // "pengumuman" atau tipe lain
                    val title = intent.getStringExtra("title") ?: "Pengumuman Koperasi"
                    val msg = intent.getStringExtra("message") ?: "Ada pengumuman baru dari koperasi."
                    NotificationHelper.showNotification(context, 1002, title, msg)
                }
            }
            return
        }

        // =========================
        // Mode B: auto (tanpa type)
        // =========================

        // Kode pegawai bisa diambil dari PrefManager jika sudah login
        val pref = PrefManager(context)
        val kodePegawai = pref.getKodePegawai() ?: "EMP001"

        // 1) Cek due dalam 3 hari ke depan
        val upcoming = DummyUserData.getUpcomingDues(kodePegawai, daysAhead = 3)
        if (upcoming.isNotEmpty()) {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val lines = upcoming.joinToString(separator = "\n") { r ->
                "Pinj #${r.pinjamanId} • ${r.dueDate}"
            }
            NotificationHelper.showDueNotification(
                context = context,
                title = "Pengingat Jatuh Tempo",
                message = "Ada cicilan jatuh tempo ≤ 3 hari:\n$lines"
            )
        }

        // 2) (Opsional) cek pengumuman terbaru vs lastSeen
        val latest = DummyUserData.pengumumanList.maxByOrNull { it.tanggal }
        if (latest != null) {
            val lastSeen = pref.getLastSeenAnnouncementDate()
            if (lastSeen == null || latest.tanggal > lastSeen) {
                NotificationHelper.showAnnouncementNotification(
                    context = context,
                    title = "Pengumuman Baru",
                    message = "${latest.judul} (${latest.tanggal})"
                )
                pref.setLastSeenAnnouncementDate(latest.tanggal)
            }
        }
    }
}
