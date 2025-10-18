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
import com.example.projek_map.utils.NotificationHelper
import com.example.projek_map.utils.AlarmReceiver
import com.google.android.material.button.MaterialButton
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
        val btnKirimPengumuman = view.findViewById<MaterialButton>(R.id.btnKirimPengumuman)
        chartKeuangan = view.findViewById(R.id.chartKeuangan)

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

        // === Navigasi: arahkan berbeda untuk admin vs user (tanpa ubah struktur metode) ===
        cardSimpanan.setOnClickListener {
            val fragment = if (isAdmin) KelolaSimpananFragment() else SimpananFragment()
            val bundle = fragment.arguments ?: Bundle()
            bundle.putBoolean("isAdmin", isAdmin)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        cardPinjaman.setOnClickListener {
            val fragment = if (isAdmin) KelolaPinjamanFragment() else PinjamanFragment()
            val bundle = fragment.arguments ?: Bundle()
            bundle.putBoolean("isAdmin", isAdmin)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        cardLaporan.setOnClickListener {
            val fragment = LaporanFragment()
            val bundle = fragment.arguments ?: Bundle()
            bundle.putBoolean("isAdmin", isAdmin)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        // ====== UBAH LABEL & ARAH KLIK UNTUK KARTU PROFIL ======
        // TextView label di dalam cardProfil tidak punya id di XML.
        // Kita ambil lewat hierarchy: CardView -> LinearLayout -> (0: ImageView, 1: TextView)
        val profilTitleView: TextView? = try {
            val containerView = cardProfil.getChildAt(0) as? ViewGroup
            containerView?.let { it.getChildAt(1) as? TextView }
        } catch (_: Throwable) { null }

        if (isAdmin) {
            // Ubah label jadi "Kelola Pengguna"
            profilTitleView?.text = "Kelola Pengguna"

            // Klik -> KelolaAnggotaFragment
            cardProfil.setOnClickListener {
                val fragment = KelolaAnggotaFragment()
                val bundle = fragment.arguments ?: Bundle()
                bundle.putBoolean("isAdmin", true)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            // Pastikan label tetap "Profil Pengguna"
            profilTitleView?.text = "Profil Pengguna"

            // Klik -> ProfileFragment (user)
            cardProfil.setOnClickListener {
                val fragment = ProfileFragment()
                val bundle = fragment.arguments ?: Bundle()
                bundle.putBoolean("isAdmin", false)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        // ====== END UBAH LABEL & ARAH KLIK ======

        // 🔔 Jadwal notifikasi jatuh tempo
        scheduleDailyJatuhTempo(requireContext(), 9, 0)

        // 🔔 Kirim pengumuman test (admin)
        if (isAdmin) {
            btnKirimPengumuman.visibility = View.VISIBLE
            btnKirimPengumuman.setOnClickListener {
                NotificationHelper.showNotification(
                    requireContext(),
                    2001,
                    "Pengumuman Koperasi",
                    "Halo anggota, ada pengumuman penting dari pengurus. Cek aplikasi."
                )
            }
        }

        // === 🟢 Tambahkan: Inisialisasi Grafik Keuangan ===
        setupChartKeuangan()
    }

    private fun setupChartKeuangan() {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        // Data dummy simpanan & pinjaman (bisa diambil dari DummyUserData nanti)
        val simpananValues = listOf(1_000_000, 1_300_000, 1_500_000, 2_000_000, 2_200_000, 2_500_000)
        val pinjamanValues = listOf(500_000, 700_000, 900_000, 1_200_000, 1_300_000, 1_500_000)

        val entriesSimpanan = simpananValues.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
        val entriesPinjaman = pinjamanValues.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }

        val dataSetSimpanan = LineDataSet(entriesSimpanan, "Simpanan").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[0])
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
        }

        val dataSetPinjaman = LineDataSet(entriesPinjaman, "Pinjaman").apply {
            color = ColorTemplate.MATERIAL_COLORS[2]
            setCircleColor(ColorTemplate.MATERIAL_COLORS[2])
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
        }

        val lineData = LineData(dataSetSimpanan, dataSetPinjaman)
        chartKeuangan.data = lineData

        chartKeuangan.xAxis.apply {
            valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(months)
            granularity = 1f
            textSize = 10f
        }

        chartKeuangan.axisLeft.textSize = 10f
        chartKeuangan.axisRight.isEnabled = false

        chartKeuangan.description = Description().apply {
            text = "Perkembangan Keuangan"
            textSize = 10f
        }

        chartKeuangan.animateX(1000)
        chartKeuangan.invalidate()
    }

    private fun scheduleDailyJatuhTempo(context: Context, hour: Int, minute: Int) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("type", "jatuh_tempo")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                3001,
                intent,
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
