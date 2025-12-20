package com.example.projek_map.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.utils.AlarmReceiver
import com.example.projek_map.utils.PrefManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs

class DashboardFragment : Fragment() {

    private var isAdmin: Boolean = false
    private lateinit var chartKeuangan: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isAdmin = arguments?.getBoolean("isAdmin", false) ?: false

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)

        val cardSimpanan = view.findViewById<CardView>(R.id.cardSimpanan)
        val cardPinjaman = view.findViewById<CardView>(R.id.cardPinjaman)
        val cardLaporan = view.findViewById<CardView>(R.id.cardLaporan)
        val cardProfil = view.findViewById<CardView>(R.id.cardProfil)
        val cardKas = view.findViewById<CardView>(R.id.cardKas)
        val cardLaporanAdmin = view.findViewById<CardView>(R.id.cardLaporanAdmin)
        val cardML = view.findViewById<CardView>(R.id.cardML)

        chartKeuangan = view.findViewById(R.id.chartKeuangan)

        val pref = PrefManager(requireContext())
        val kodePegawai = pref.getKodePegawai().orEmpty()
        val nama = pref.getUserName().orEmpty()

        if (isAdmin) {
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
        } else {
            tvWelcome.text = if (nama.isNotBlank()) "Halo, $nama" else "Halo, Pengguna"
            tvUserId.text = if (kodePegawai.isNotBlank()) "Kode Pegawai: $kodePegawai" else "Kode Pegawai: -"
            imgProfile.setImageResource(R.drawable.ic_profil)
        }

        // ====== Label kartu sesuai role ======
        if (isAdmin) {
            setCardTitle(cardSimpanan, "Kelola Simpanan")
            setCardTitle(cardPinjaman, "Kelola Pinjaman")
            setCardTitle(cardLaporan, "Pengaturan Koperasi")
        } else {
            setCardTitle(cardSimpanan, "Simpanan")
            setCardTitle(cardPinjaman, "Pinjaman")
            setCardTitle(cardLaporan, "Laporan")
        }

        // Icon laporan sesuai role
        findFirstImageView(cardLaporan)?.setImageResource(
            if (isAdmin) R.drawable.ic_setting else R.drawable.ic_laporan
        )

        // === Navigasi ===
        cardSimpanan.setOnClickListener {
            val fragment = if (isAdmin) KelolaSimpananFragment() else SimpananFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply { putBoolean("isAdmin", isAdmin) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        cardPinjaman.setOnClickListener {
            val fragment = if (isAdmin) KelolaPinjamanFragment() else PinjamanFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply { putBoolean("isAdmin", isAdmin) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        cardLaporan.setOnClickListener {
            val fragment: Fragment = if (isAdmin) KelolaPengurusFragment() else LaporanFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply { putBoolean("isAdmin", isAdmin) }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        // (profilTitleView dipakai di logic lama kamu, aku biarkan pola yang sama)
        val profilTitleView = findFirstTextView(cardProfil)

        if (isAdmin) {
            // ADMIN: cardProfil jadi "Kelola Pengguna"
            profilTitleView?.text = "Kelola Pengguna"
            cardProfil.setOnClickListener {
                val fragment = KelolaAnggotaFragment().apply {
                    arguments = (arguments ?: Bundle()).apply { putBoolean("isAdmin", true) }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            // ADMIN: cardML tampil untuk Smart ML
            cardML.visibility = View.VISIBLE
            setCardTitle(cardML, "Smart ML")
            cardML.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SmartMlFragment())
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            // USER: cardProfil diganti jadi Smart ML
            profilTitleView?.text = "Smart ML"
            cardProfil.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SmartMlFragment())
                    .addToBackStack(null)
                    .commit()
            }

            // USER: cardML disembunyikan
            cardML.visibility = View.GONE
        }

        // ====== Card admin khusus ======
        if (isAdmin) {
            cardKas.visibility = View.VISIBLE
            setCardTitle(cardKas, "Kelola Kas")
            cardKas.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, KelolaKasFragment())
                    .addToBackStack(null)
                    .commit()
            }

            cardLaporanAdmin.visibility = View.VISIBLE
            setCardTitle(cardLaporanAdmin, "Laporan Koperasi")
            cardLaporanAdmin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LaporanAdminFragment())
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            cardKas.visibility = View.GONE
            cardLaporanAdmin.visibility = View.GONE
        }

        // 🔔 Jadwal notifikasi jatuh tempo
        scheduleDailyJatuhTempo(requireContext(), 9, 0)

        // === Grafik Keuangan (rekap bar chart, tanpa zoom) ===
        setupChartStyle()

        if (isAdmin) {
            setupChartAdminFromApi()
        } else {
            setupChartUserFromApi(kodePegawai)
        }
    }

    private fun setupChartStyle() {
        chartKeuangan.description = Description().apply { text = "" }
        chartKeuangan.setFitBars(true)

        // Matikan zoom biar dosen kamu tidak nyuruh “coba zoom” lagi
        chartKeuangan.setScaleEnabled(false)
        chartKeuangan.setPinchZoom(false)
        chartKeuangan.isDoubleTapToZoomEnabled = false

        chartKeuangan.axisRight.isEnabled = false
        chartKeuangan.axisLeft.axisMinimum = 0f

        // Format angka ringkas di axis Y
        val axisFmt = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return formatRupiahShort(value.toDouble())
            }
        }
        chartKeuangan.axisLeft.valueFormatter = axisFmt

        // X label: Simpanan, Pinjaman
        chartKeuangan.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter(listOf("Simpanan", "Pinjaman"))
        }

        chartKeuangan.legend.isEnabled = false
    }

    private fun setupChartUserFromApi(kodePegawai: String) {
        if (kodePegawai.isBlank()) {
            renderBarChart(0.0, 0.0, "Kode pegawai kosong")
            return
        }

        lifecycleScope.launch {
            try {
                val api = ApiClient.apiService

                val simpananResp = api.getSimpanan(kodePegawai)
                val pinjamanResp = api.getPinjaman(kodePegawai)

                val totalSimpanan =
                    simpananResp.body()?.data?.let { s ->
                        (s.simpananPokok ?: 0.0) + (s.simpananWajib ?: 0.0) + (s.simpananSukarela ?: 0.0)
                    } ?: 0.0

                // Total pinjaman AKTIF saja (status != lunas)
                val totalPinjamanAktif =
                    pinjamanResp.body()?.data
                        ?.filter { it.status.lowercase() != "lunas" }
                        ?.sumOf { it.jumlah.toDouble() }
                        ?: 0.0

                renderBarChart(totalSimpanan, totalPinjamanAktif, "")

            } catch (e: Exception) {
                renderBarChart(0.0, 0.0, e.message ?: "Gagal ambil data")
            }
        }
    }

    private fun setupChartAdminFromApi() {
        lifecycleScope.launch {
            try {
                val api = ApiClient.apiService
                val resp = api.getLaporanAdmin()
                val data = resp.body()?.data

                val totalSimpananAll = data?.totalSimpananAll ?: 0.0
                val totalPinjamanAll = data?.totalPinjamanAll ?: 0.0

                renderBarChart(totalSimpananAll, totalPinjamanAll, "")

            } catch (e: Exception) {
                renderBarChart(0.0, 0.0, e.message ?: "Gagal ambil data admin")
            }
        }
    }

    private fun renderBarChart(totalSimpanan: Double, totalPinjaman: Double, label: String) {
        val entries = arrayListOf(
            BarEntry(0f, totalSimpanan.toFloat()),
            BarEntry(1f, totalPinjaman.toFloat())
        )

        val dataSet = BarDataSet(entries, label.ifBlank { "Rekap" }).apply {
            colors = listOf(
                ColorTemplate.MATERIAL_COLORS[0],
                ColorTemplate.MATERIAL_COLORS[1]
            )
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return formatRupiahShort(value.toDouble())
                }
            }
        }

        chartKeuangan.data = BarData(dataSet).apply {
            barWidth = 0.55f
        }

        chartKeuangan.invalidate()
    }

    /**
     * Format ringkas:
     * - Rp 1,2 JT
     * - Rp 32,7 JT
     * - Rp 950 RB
     */
    private fun formatRupiahShort(v: Double): String {
        val value = abs(v)

        return when {
            value >= 1_000_000_000 -> {
                val num = value / 1_000_000_000.0
                "Rp ${String.format("%.1f", num).replace('.', ',')} M"
            }
            value >= 1_000_000 -> {
                val num = value / 1_000_000.0
                "Rp ${String.format("%.1f", num).replace('.', ',')} JT"
            }
            value >= 1_000 -> {
                val num = value / 1_000.0
                "Rp ${String.format("%.0f", num)} RB"
            }
            else -> {
                "Rp ${String.format("%.0f", value)}"
            }
        }
    }

    private fun setCardTitle(card: CardView, title: String) {
        findFirstTextView(card)?.text = title
    }

    private fun findFirstTextView(view: View): TextView? {
        if (view is TextView) return view
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val found = findFirstTextView(view.getChildAt(i))
                if (found != null) return found
            }
        }
        return null
    }

    private fun findFirstImageView(view: View): ImageView? {
        if (view is ImageView) return view
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val found = findFirstImageView(view.getChildAt(i))
                if (found != null) return found
            }
        }
        return null
    }

    private fun scheduleDailyJatuhTempo(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
