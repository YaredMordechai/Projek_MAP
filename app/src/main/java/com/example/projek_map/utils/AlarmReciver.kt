package com.example.projek_map.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.projek_map.data.DummyUserData
import java.text.SimpleDateFormat
import java.text.NumberFormat
import java.util.Locale
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.utils.NotificationHelper

// 🔔 AlarmReceiver gabungan: dukung 2 mode sekaligus
// - Mode A: intent extra "type" = "jatuh_tempo" / "pengumuman"  -> kirim notifikasi sesuai extra
// - Mode B: TANPA extra "type" -> cek auto: due cicilan & pengumuman baru dari DummyUserData
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val type = intent?.getStringExtra("type")

        if (type == "keputusan_pinjaman") {
            // Hanya tampilkan di device milik pegawai yang jadi target
            val targetKode = intent.getStringExtra("kodePegawai")
            val currentKode = PrefManager(context).getKodePegawai()
            if (targetKode != null && targetKode != currentKode) {
                return // bukan device pemilik pinjaman → jangan tampilkan notifikasi
            }

            val decision = intent.getStringExtra("decision") ?: "diproses"
            val pinjamanId = intent.getIntExtra("pinjamanId", -1)
            val jumlah = intent.getIntExtra("jumlah", 0)

            val title = if (decision == "disetujui") "Pinjaman Disetujui" else "Pinjaman Ditolak"
            val jumlahFmt = String.format("%,d", jumlah).replace(',', '.')
            val msg = if (pinjamanId != -1)
                "Pinjaman #$pinjamanId sebesar Rp $jumlahFmt telah $decision."
            else
                "Pengajuan pinjaman kamu telah $decision."

            // ID notifikasi dibedakan per pinjaman agar tidak saling menimpa
            NotificationHelper.showNotification(
                context,
                2000 + (if (pinjamanId >= 0) pinjamanId else 0),
                title,
                msg
            )
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
