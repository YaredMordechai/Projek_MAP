package com.example.projek_map.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.projek_map.api.ApiClient
import com.example.projek_map.data.PinjamanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 🔔 AlarmReceiver gabungan: dukung 2 mode sekaligus
// - Mode A (event-driven): intent extra "type" =
//     • "keputusan_pinjaman"  -> push keputusan per user (filter by kodePegawai)
//     • "pengumuman_baru"     -> push pengumuman langsung (real-time)
//     • "jatuh_tempo"         -> push pengingat jatuh tempo langsung (opsional)
// - Mode B (auto): TANPA extra "type" -> cek due cicilan & pengumuman baru dari DATABASE/API (bukan Dummy)
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
            if (targetKode != null && targetKode != currentKode) return

            val decision = intent.getStringExtra("decision") ?: "diproses"
            val pinjamanId = intent.getIntExtra("pinjamanId", -1)
            val jumlah = intent.getIntExtra("jumlah", 0)

            val title = if (decision == "disetujui") "Pinjaman Disetujui" else "Pinjaman Ditolak"
            val jumlahFmt = NumberFormat.getIntegerInstance(Locale("id", "ID")).format(jumlah)
            val msg = if (pinjamanId != -1)
                "Pinjaman #$pinjamanId sebesar Rp $jumlahFmt telah $decision."
            else
                "Pengajuan pinjaman kamu telah $decision."

            val notifId = 2000 + (if (pinjamanId >= 0) pinjamanId else 0)
            NotificationHelper.showNotification(context, notifId, title, msg)
            return
        }

        // A2) Pengumuman baru (real-time push dari admin)
        if (type == "pengumuman_baru") {
            val pref = PrefManager(context)
            if (pref.getIsAdmin()) return

            val title = intent.getStringExtra("title") ?: "Pengumuman Koperasi"
            val message = intent.getStringExtra("message") ?: "Ada pengumuman baru dari koperasi."

            NotificationHelper.showAnnouncementNotification(context, title, message)

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
        // Mode B: auto (tanpa type) -> pakai API/DB
        // =========================

        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = PrefManager(context)
                val kodePegawai = pref.getKodePegawai().orEmpty()
                if (kodePegawai.isBlank()) return@launch

                val repo = PinjamanRepository()

                // 1) Pengingat jatuh tempo (best-effort, dueDay default tanggal 10)
                //    NOTE: tanpa dueDate dari backend, ini hanya estimasi.
                try {
                    val pinjamanResp = repo.getPinjamanUser(kodePegawai)
                    if (pinjamanResp.success) {
                        val aktif = (pinjamanResp.data ?: emptyList()).filter {
                            val s = it.status.lowercase()
                            s == "disetujui" || s == "aktif"
                        }

                        val todayCal = Calendar.getInstance()
                        val y = todayCal.get(Calendar.YEAR)
                        val m = todayCal.get(Calendar.MONTH)
                        val d = todayCal.get(Calendar.DAY_OF_MONTH)
                        val DUE_DAY = 10

                        val dueCal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, DUE_DAY)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        if (d > DUE_DAY) dueCal.add(Calendar.MONTH, 1)

                        val diffMs = dueCal.timeInMillis - todayCal.timeInMillis
                        val diffDays = kotlin.math.ceil(diffMs / (1000.0 * 60 * 60 * 24)).toInt()

                        if (diffDays in 0..3 && aktif.isNotEmpty()) {
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                            val dueStr = sdf.format(dueCal.time)

                            // ambil cicilan rekomendasi dari rincian (anuitas) pinjaman pertama (biar tidak berat)
                            val contoh = aktif.first()
                            val rinc = repo.getRincianPinjamanDb(contoh.id, "anuitas")
                            val nominal = if (rinc.success) (rinc.data?.cicilanPerBulan ?: 0) else 0

                            val msg = if (nominal > 0)
                                "Ada cicilan jatuh tempo ≤ 3 hari (estimasi tgl $dueStr). Rekomendasi: Rp ${
                                    NumberFormat.getIntegerInstance(Locale("id","ID")).format(nominal)
                                }"
                            else
                                "Ada cicilan jatuh tempo ≤ 3 hari (estimasi tgl $dueStr)."

                            NotificationHelper.showDueNotification(
                                context = context,
                                title = "Pengingat Jatuh Tempo",
                                message = msg
                            )
                        }
                    }
                } catch (_: Throwable) {
                    // no-op biar receiver tidak crash
                }

                // 2) Pengumuman terbaru vs lastSeen -> ambil dari API (bukan dummy)
                try {
                    val resp = ApiClient.apiService.getPengumuman()
                    val body = resp.body()
                    if (resp.isSuccessful && body?.success == true) {
                        val latest = (body.data ?: emptyList()).maxByOrNull { it.tanggal }
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
                } catch (_: Throwable) {
                    // no-op
                }

            } finally {
                pending.finish()
            }
        }
    }
}
