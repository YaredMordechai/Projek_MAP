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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var isAdmin: Boolean = false
    private lateinit var chartKeuangan: LineChart

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

        // ====== PROFIL / ML sesuai role ======
        val profilTitleView = findFirstTextView(cardProfil)

        if (isAdmin) {
            // ADMIN: cardProfil tetap Kelola Pengguna/Anggota
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

        // === Grafik Keuangan (ambil dari API) ===
        if (isAdmin) {
            setupChartAdminPlaceholder()
        } else {
            setupChartUserFromApi(kodePegawai)
        }
    }

    private fun setupChartAdminPlaceholder() {
        val entries = (0..11).map { i -> Entry(i.toFloat(), 0f) }
        val ds = LineDataSet(entries, "Admin Chart").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
        }
        chartKeuangan.data = LineData(ds)
        chartKeuangan.description = Description().apply { text = "" }
        chartKeuangan.invalidate()
    }

    private fun setupChartUserFromApi(kodePegawai: String) {
        if (kodePegawai.isBlank()) {
            setupChartEmpty("Kode pegawai kosong")
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

                val totalPinjaman =
                    pinjamanResp.body()?.data?.sumOf { it.jumlah.toDouble() } ?: 0.0

                val entriesSimpanan = (0..11).map { i -> Entry(i.toFloat(), totalSimpanan.toFloat()) }
                val entriesPinjaman = (0..11).map { i -> Entry(i.toFloat(), totalPinjaman.toFloat()) }

                val dsSimpanan = LineDataSet(entriesSimpanan, "Total Simpanan").apply {
                    color = ColorTemplate.MATERIAL_COLORS[0]
                    setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
                }

                val dsPinjaman = LineDataSet(entriesPinjaman, "Total Pinjaman").apply {
                    color = ColorTemplate.MATERIAL_COLORS[1]
                    setCircleColor(ColorTemplate.MATERIAL_COLORS[1])
                }

                chartKeuangan.data = LineData(dsSimpanan, dsPinjaman)
                chartKeuangan.description = Description().apply { text = "" }
                chartKeuangan.invalidate()

            } catch (e: Exception) {
                setupChartEmpty(e.message ?: "Gagal ambil data")
            }
        }
    }

    private fun setupChartEmpty(reason: String) {
        val entries = (0..11).map { i -> Entry(i.toFloat(), 0f) }
        val ds = LineDataSet(entries, reason).apply {
            color = ColorTemplate.MATERIAL_COLORS[2]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[2])
        }
        chartKeuangan.data = LineData(ds)
        chartKeuangan.description = Description().apply { text = "" }
        chartKeuangan.invalidate()
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
