package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.example.projek_map.utils.PrefManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.NumberFormat
import java.util.*
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.api.ApiClient
import kotlinx.coroutines.launch

class LaporanFragment : Fragment() {

    private lateinit var txtTotalSimpanan: TextView
    private lateinit var txtTotalPinjaman: TextView
    private lateinit var txtTotalAngsuran: TextView
    private lateinit var spinnerBulan: Spinner
    private lateinit var chartRekapKeuangan: BarChart

    private var kodePegawaiAktif: String? = null

    // ✅ cache monthly dari DB untuk chart
    private var lastMonthlySimpanan: List<Double>? = null
    private var lastMonthlyAngsuran: List<Double>? = null
    private var lastYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        // --- Inisialisasi View ---
        val btnBackDashboard = view.findViewById<ImageButton>(R.id.btnBackDashboard)
        txtTotalSimpanan = view.findViewById(R.id.txtTotalSimpanan)
        txtTotalPinjaman = view.findViewById(R.id.txtTotalPinjaman)
        txtTotalAngsuran = view.findViewById(R.id.txtTotalAngsuran)
        spinnerBulan = view.findViewById(R.id.spinnerBulan)
        chartRekapKeuangan = view.findViewById(R.id.chartRekapKeuangan)

        // --- Tombol kembali ke Dashboard ---
        btnBackDashboard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DashboardFragment())
                .commit()
        }

        // --- Ambil data user aktif dari PrefManager ---
        val pref = PrefManager(requireContext())
        kodePegawaiAktif = pref.getKodePegawai() ?: "EMP001"

        setupSpinner()
        setupChart()

        val cal = Calendar.getInstance()
        val bulanSekarang = cal.get(Calendar.MONTH) + 1
        lastYear = cal.get(Calendar.YEAR)

        // ✅ ambil DB dulu
        loadLaporanFromApi(bulanSekarang, lastYear)

        return view
    }

    private fun setupSpinner() {
        val bulanList = listOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bulanList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBulan.adapter = adapter

        // Set spinner ke bulan sekarang
        val bulanSekarang = Calendar.getInstance().get(Calendar.MONTH)
        spinnerBulan.setSelection(bulanSekarang)

        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val bulan = position + 1
                loadLaporanFromApi(bulan, lastYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // === Update nilai total (TextView) [DUMMY - fallback] ===
    private fun updateLaporan(bulan: Int) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val kode = kodePegawaiAktif ?: return

        val totalSimpanan = DummyUserData.getTotalSimpanan(kode)
        val totalPinjaman = DummyUserData.getTotalPinjamanAktif(kode)
        val totalAngsuran = DummyUserData.getTotalAngsuranBulanan(kode, bulan)

        txtTotalSimpanan.text = formatter.format(totalSimpanan)
        txtTotalPinjaman.text = formatter.format(totalPinjaman)
        txtTotalAngsuran.text = formatter.format(totalAngsuran)

        // chart tetap jalan pakai dummy
        lastMonthlySimpanan = null
        lastMonthlyAngsuran = null
        updateChart()
    }

    // === Setup chart awal ===
    private fun setupChart() {
        chartRekapKeuangan.apply {
            description = Description().apply {
                text = "Grafik Rekap Keuangan Koperasi"
                textSize = 10f
            }
            axisRight.isEnabled = false
            setFitBars(true)
            animateY(1000)
        }
        updateChart()
    }

    // ✅ === Ambil laporan dari API (DB) ===
    private fun loadLaporanFromApi(bulan: Int, year: Int) {
        lifecycleScope.launch {
            val kode = kodePegawaiAktif ?: return@launch
            try {
                // ⚠️ Pastikan KoperasiApiService punya getLaporanUser(kode, bulan, year)
                val resp = ApiClient.apiService.getLaporanUser(kode, bulan, year)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data
                    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

                    txtTotalSimpanan.text = formatter.format(d.totalSimpanan)
                    txtTotalPinjaman.text = formatter.format(d.totalPinjamanAktif)
                    txtTotalAngsuran.text = formatter.format(d.totalAngsuranBulanan)

                    // cache monthly utk chart
                    lastMonthlySimpanan = d.monthlySimpanan
                    lastMonthlyAngsuran = d.monthlyAngsuran

                    updateChart()
                } else {
                    // fallback ke dummy
                    updateLaporan(bulan)
                }
            } catch (_: Exception) {
                // fallback ke dummy
                updateLaporan(bulan)
            }
        }
    }

    // === Update data chart sesuai bulan ===
    private fun updateChart() {
        val bulanList = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
        val kode = kodePegawaiAktif ?: return

        val monthlyS = lastMonthlySimpanan
        val monthlyA = lastMonthlyAngsuran

        val simpananData: List<Float>
        val angsuranData: List<Float>

        // ✅ kalau DB ada monthly 12 data, pakai DB
        if (monthlyS != null && monthlyA != null && monthlyS.size == 12 && monthlyA.size == 12) {
            simpananData = monthlyS.map { it.toFloat() }
            angsuranData = monthlyA.map { it.toFloat() }
        } else {
            // fallback dummy (behavior lama)
            simpananData = bulanList.indices.map { DummyUserData.getTotalSimpanan(kode).toFloat() + it * 100000 }
            angsuranData = bulanList.indices.map { DummyUserData.getTotalAngsuranBulanan(kode, it + 1).toFloat() }
        }

        val entriesSimpanan = simpananData.mapIndexed { i, v -> BarEntry(i.toFloat(), v) }
        val entriesAngsuran = angsuranData.mapIndexed { i, v -> BarEntry(i.toFloat(), v) }

        val dataSetSimpanan = BarDataSet(entriesSimpanan, "Simpanan").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            valueTextSize = 9f
        }

        val dataSetAngsuran = BarDataSet(entriesAngsuran, "Angsuran").apply {
            color = ColorTemplate.MATERIAL_COLORS[1]
            valueTextSize = 9f
        }

        val barData = BarData(dataSetSimpanan, dataSetAngsuran)
        barData.barWidth = 0.3f
        chartRekapKeuangan.data = barData

        val xAxis = chartRekapKeuangan.xAxis
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(bulanList)
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawGridLines(false)

        chartRekapKeuangan.groupBars(0f, 0.4f, 0.05f)
        chartRekapKeuangan.invalidate()
    }
}
