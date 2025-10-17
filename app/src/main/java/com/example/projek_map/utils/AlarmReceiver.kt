package com.example.projek_map.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.projek_map.data.DummyUserData
import java.text.NumberFormat
import java.util.Locale

// 🔔 AlarmReceiver gabungan: dukung 2 mode sekaligus
// - Mode A (event-driven): intent extra "type" =
//     • "keputusan_pinjaman"  -> push keputusan per user (filter by kodePegawai)
//     • "pengumuman_baru"     -> push pengumuman langsung (real-time)
//     • "jatuh_tempo"         -> push pengingat jatuh tempo langsung (opsional)
// - Mode B (auto): TANPA extra "type" -> cek due cicilan & pengumuman baru dari DummyUserData
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val type = intent?.getStringExtra("type")

        // =========================
        // Mode A: event-driven
        // =========================

        // A1) Keputusan pinjaman (dengan filter device pemilik pinjaman)
        if (type == "keputusan_pinjaman") {
            val targetKode = intent.getStringExtra("kodePegawai")
            val currentKode = PrefManager(context).getKodePegawai()
            if (targetKode != null && targetKode != currentKode) {
                return // bukan device milik pemohon pinjaman → jangan tampilkan
            }

            val decision = intent.getStringExtra("decision") ?: "diproses"
            val pinjamanId = intent.getIntExtra("pinjamanId", -1)
            val jumlah = intent.getIntExtra("jumlah", 0)

            val title = if (decision == "disetujui") "Pinjaman Disetujui" else "Pinjaman Ditolak"
            val jumlahFmt = NumberFormat.getIntegerInstance(Locale("id", "ID")).format(jumlah)
            val msg = if (pinjamanId != -1)
                "Pinjaman #$pinjamanId sebesar Rp $jumlahFmt telah $decision."
            else
                "Pengajuan pinjaman kamu telah $decision."

            // ID notifikasi dibedakan per pinjaman agar tidak saling menimpa
            val notifId = 2000 + (if (pinjamanId >= 0) pinjamanId else 0)
            NotificationHelper.showNotification(
                context,
                notifId,
                title,
                msg
            )
            return
        }

        // A2) Pengumuman baru (real-time push dari admin)
        if (type == "pengumuman_baru") {
            val pref = PrefManager(context)
            if (pref.getIsAdmin()) return // admin (pembuat) tidak perlu menerima notif pengumuman

            val title = intent.getStringExtra("title") ?: "Pengumuman Koperasi"
            val message = intent.getStringExtra("message") ?: "Ada pengumuman baru dari koperasi."

            NotificationHelper.showAnnouncementNotification(
                context = context,
                title = title,
                message = message
            )

            // (opsional) tandai sebagai sudah dilihat berdasarkan tanggal yang ikut di intent
            intent.getStringExtra("tanggal")?.let { tanggal ->
                pref.setLastSeenAnnouncementDate(tanggal)
            }
            return
        }

        // A3) (Opsional) push khusus jatuh tempo dari intent
        if (type == "jatuh_tempo") {
            val message = intent.getStringExtra("message")
                ?: "Ada cicilan yang menjelang jatuh tempo. Cek aplikasi koperasi sekarang."
            NotificationHelper.showDueNotification(
                context = context,
                title = "Pengingat Jatuh Tempo",
                message = message
            )
            return
        }

        // =========================
        // Mode B: auto (tanpa type)
        // =========================

        val pref = PrefManager(context)
        val kodePegawai = pref.getKodePegawai() ?: "EMP001"

        // 1) Cek due dalam 3 hari ke depan
        val upcoming = DummyUserData.getUpcomingDues(kodePegawai, daysAhead = 3)
        if (upcoming.isNotEmpty()) {
            val lines = upcoming.joinToString(separator = "\n") { r ->
                "Pinj #${r.pinjamanId} • ${r.dueDate}"
            }
            NotificationHelper.showDueNotification(
                context = context,
                title = "Pengingat Jatuh Tempo",
                message = "Ada cicilan jatuh tempo ≤ 3 hari:\n$lines"
            )
        }

        // 2) Cek pengumuman terbaru vs lastSeen
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
