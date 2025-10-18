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
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.utils.AlarmReceiver
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
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
        chartKeuangan = view.findViewById(R.id.chartKeuangan)
        val cardKas = view.findViewById<CardView>(R.id.cardKas)
        val cardLaporanAdmin = view.findViewById<CardView>(R.id.cardLaporanAdmin)

        val pref = PrefManager(requireContext())

        if (isAdmin) {
            tvWelcome.text = "Halo, Admin"
            tvUserId.text = ""
        } else {
            val loggedEmail = pref.getEmail() ?: ""
            val loggedKode = pref.getKodePegawai() ?: ""

            val currentUser = DummyUserData.users.find {
                it.email.equals(loggedEmail, ignoreCase = true) || it.kodePegawai == loggedKode
            }

            if (currentUser != null) {
                tvWelcome.text = "Halo, ${currentUser.nama}"
                tvUserId.text = "Kode Pegawai: ${currentUser.kodePegawai}"
            } else {
                tvWelcome.text = "Halo, Pengguna"
                tvUserId.text = "Kode Pegawai: -"
            }

            imgProfile.setImageResource(R.drawable.ic_profil)
        }

        // ====== Ubah label kartu Simpanan & Pinjaman sesuai role ======
        if (isAdmin) {
            setCardTitle(cardSimpanan, "Kelola Simpanan")
            setCardTitle(cardPinjaman, "Kelola Pinjaman")
            setCardTitle(cardLaporan, "Pengaturan Koperasi")
        } else {
            setCardTitle(cardSimpanan, "Simpanan")
            setCardTitle(cardPinjaman, "Pinjaman")
            setCardTitle(cardLaporan, "Laporan")
        }

        // 🔁 Ganti ikon card laporan sesuai role
        findFirstImageView(cardLaporan)?.setImageResource(
            if (isAdmin) R.drawable.ic_setting else R.drawable.ic_laporan
        )

        // === Navigasi ===
        cardSimpanan.setOnClickListener {
            val fragment = if (isAdmin) KelolaSimpananFragment() else SimpananFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply {
                putBoolean("isAdmin", isAdmin)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        cardPinjaman.setOnClickListener {
            val fragment = if (isAdmin) KelolaPinjamanFragment() else PinjamanFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply {
                putBoolean("isAdmin", isAdmin)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        cardLaporan.setOnClickListener {
            val fragment: Fragment = if (isAdmin) KelolaPengurusFragment() else LaporanFragment()
            fragment.arguments = (fragment.arguments ?: Bundle()).apply {
                putBoolean("isAdmin", isAdmin)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        val profilTitleView = findFirstTextView(cardProfil)
        if (isAdmin) {
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
        } else {
            profilTitleView?.text = "Profil Pengguna"
            cardProfil.setOnClickListener {
                val fragment = ProfileFragment().apply {
                    arguments = (arguments ?: Bundle()).apply { putBoolean("isAdmin", false) }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // ====== Card Admin khusus ======
        if (isAdmin) {
            cardKas.visibility = View.VISIBLE
            setCardTitle(cardKas, "Kelola Kas")
            cardKas.setOnClickListener {
                val fragment = KelolaKasFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            cardLaporanAdmin.visibility = View.VISIBLE
            setCardTitle(cardLaporanAdmin, "Laporan Koperasi")
            cardLaporanAdmin.setOnClickListener {
                val fragment = LaporanAdminFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            cardKas.visibility = View.GONE
            cardLaporanAdmin.visibility = View.GONE
        }

        // 🔔 Jadwal notifikasi jatuh tempo
        scheduleDailyJatuhTempo(requireContext(), 9, 0)

        // === Grafik Keuangan ===
        val kodePegawai = pref.getKodePegawai() ?: ""
        if (isAdmin) setupChartKeuanganAdmin() else setupChartKeuanganUser(kodePegawai)
    }

    // ===============================================
    // ======== CHART UNTUK ADMIN DAN USER ============
    // ===============================================

    private fun setupChartKeuanganAdmin() {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        val totalSimpananPerBulan = MutableList(12) { 0.0 }
        val totalPinjamanPerBulan = MutableList(12) { 0.0 }

        // Total simpanan seluruh anggota per bulan
        DummyUserData.historiSimpananList.forEach { s ->
            val bulan = try { s.tanggal.substring(5, 7).toInt() - 1 } catch (e: Exception) { 0 }
            if (bulan in 0..11) totalSimpananPerBulan[bulan] += s.jumlah
        }

        // Total pinjaman seluruh anggota (Disetujui/Aktif)
        DummyUserData.pinjamanList
            .filter { it.status.equals("Disetujui", true) || it.status.equals("Aktif", true) }
            .forEach { p ->
                val bulanDisetujui = 9 // Oktober (dummy)
                totalPinjamanPerBulan[bulanDisetujui] += p.jumlah
            }

        val dataSetSimpanan = LineDataSet(
            totalSimpananPerBulan.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) },
            "Total Simpanan"
        ).apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
            lineWidth = 2f
            circleRadius = 4f
        }

        val dataSetPinjaman = LineDataSet(
            totalPinjamanPerBulan.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) },
            "Total Pinjaman"
        ).apply {
            color = ColorTemplate.MATERIAL_COLORS[1]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[1])
            lineWidth = 2f
            circleRadius = 4f
        }

        chartKeuangan.data = LineData(dataSetSimpanan, dataSetPinjaman)
        chartKeuangan.xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(months)
        chartKeuangan.description = Description().apply {
            text = "Rekap Seluruh Anggota"
            textSize = 10f
        }
        chartKeuangan.animateX(1000)
        chartKeuangan.invalidate()
    }

    private fun setupChartKeuanganUser(kodePegawai: String) {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        val simpananPerBulan = MutableList(12) { 0.0 }
        val pinjamanPerBulan = MutableList(12) { 0.0 }

        // Simpanan milik user
        DummyUserData.historiSimpananList
            .filter { it.kodePegawai == kodePegawai }
            .forEach { s ->
                val bulan = try { s.tanggal.substring(5, 7).toInt() - 1 } catch (e: Exception) { 0 }
                if (bulan in 0..11) simpananPerBulan[bulan] += s.jumlah
            }

        // Pinjaman milik user (disetujui/aktif)
        DummyUserData.pinjamanList
            .filter { it.kodePegawai == kodePegawai && (it.status.equals("Disetujui", true) || it.status.equals("Aktif", true)) }
            .forEach { p ->
                val bulanDisetujui = 9 // Oktober
                pinjamanPerBulan[bulanDisetujui] += p.jumlah
            }

        val dataSetSimpanan = LineDataSet(
            simpananPerBulan.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) },
            "Simpanan Saya"
        ).apply {
            color = ColorTemplate.MATERIAL_COLORS[2]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[2])
            lineWidth = 2f
            circleRadius = 4f
        }

        val dataSetPinjaman = LineDataSet(
            pinjamanPerBulan.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) },
            "Pinjaman Saya"
        ).apply {
            color = ColorTemplate.MATERIAL_COLORS[3]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[3])
            lineWidth = 2f
            circleRadius = 4f
        }

        chartKeuangan.data = LineData(dataSetSimpanan, dataSetPinjaman)
        chartKeuangan.xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(months)
        chartKeuangan.description = Description().apply {
            text = "Aktivitas Keuangan Pribadi"
            textSize = 10f
        }
        chartKeuangan.animateX(1000)
        chartKeuangan.invalidate()
    }

    // ===============================================
    // Helper & Scheduler
    // ===============================================

    private fun setCardTitle(card: CardView, newTitle: String) {
        findFirstTextView(card)?.text = newTitle
    }

    private fun findFirstTextView(root: View?): TextView? {
        when (root) {
            is TextView -> return root
            is ViewGroup -> {
                for (i in 0 until root.childCount) {
                    val found = findFirstTextView(root.getChildAt(i))
                    if (found != null) return found
                }
            }
        }
        return null
    }

    private fun findFirstImageView(root: View?): ImageView? {
        when (root) {
            is ImageView -> return root
            is ViewGroup -> {
                for (i in 0 until root.childCount) {
                    val found = findFirstImageView(root.getChildAt(i))
                    if (found != null) return found
                }
            }
        }
        return null
    }

    private fun scheduleDailyJatuhTempo(context: Context, hour: Int, minute: Int) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("type", "jatuh_tempo")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 3001, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) add(Calendar.DATE, 1)
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}