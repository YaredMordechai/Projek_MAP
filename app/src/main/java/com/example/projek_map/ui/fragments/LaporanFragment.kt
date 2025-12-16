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
import androidx.lifecycle.lifecycleScope
import com.example.projek_map.R
import com.example.projek_map.api.ApiClient
import com.example.projek_map.utils.PrefManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class LaporanFragment : Fragment() {

    private lateinit var txtTotalSimpanan: TextView
    private lateinit var txtTotalPinjaman: TextView
    private lateinit var txtTotalAngsuran: TextView
    private lateinit var spinnerBulan: Spinner
    private lateinit var chartRekapKeuangan: BarChart

    private var kodePegawaiAktif: String? = null
    private var lastYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    private var monthlySimpanan: List<Double> = List(12) { 0.0 }
    private var monthlyAngsuran: List<Double> = List(12) { 0.0 }

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        val btnBackDashboard = view.findViewById<ImageButton>(R.id.btnBackDashboard)
        txtTotalSimpanan = view.findViewById(R.id.txtTotalSimpanan)
        txtTotalPinjaman = view.findViewById(R.id.txtTotalPinjaman)
        txtTotalAngsuran = view.findViewById(R.id.txtTotalAngsuran)
        spinnerBulan = view.findViewById(R.id.spinnerBulan)
        chartRekapKeuangan = view.findViewById(R.id.chartRekapKeuangan)

        btnBackDashboard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DashboardFragment())
                .commit()
        }

        kodePegawaiAktif = PrefManager(requireContext()).getKodePegawai() ?: "EMP001"

        val cal = Calendar.getInstance()
        lastYear = cal.get(Calendar.YEAR)

        setupSpinner()
        setupChart()

        val bulanSekarang = cal.get(Calendar.MONTH) + 1
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

        val bulanSekarangIdx = Calendar.getInstance().get(Calendar.MONTH)
        spinnerBulan.setSelection(bulanSekarangIdx)

        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadLaporanFromApi(position + 1, lastYear)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupChart() {
        chartRekapKeuangan.apply {
            description = Description().apply {
                text = "Grafik Rekap Keuangan"
                textSize = 10f
            }
            axisRight.isEnabled = false
            setFitBars(true)
            animateY(700)
        }
        updateChart()
    }

    private fun loadLaporanFromApi(bulan: Int, year: Int) {
        lifecycleScope.launch {
            val kode = kodePegawaiAktif ?: return@launch
            try {
                val resp = ApiClient.apiService.getLaporanUser(kode, bulan, year)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data

                    txtTotalSimpanan.text = rupiah.format(d.totalSimpanan)
                    txtTotalPinjaman.text = rupiah.format(d.totalPinjamanAktif)
                    txtTotalAngsuran.text = rupiah.format(d.totalAngsuranBulanan)

                    monthlySimpanan = if (d.monthlySimpanan.size == 12) d.monthlySimpanan else List(12) { 0.0 }
                    monthlyAngsuran = if (d.monthlyAngsuran.size == 12) d.monthlyAngsuran else List(12) { 0.0 }

                    updateChart()
                } else {
                    renderZero()
                }
            } catch (_: Exception) {
                renderZero()
            }
        }
    }

    private fun renderZero() {
        txtTotalSimpanan.text = rupiah.format(0)
        txtTotalPinjaman.text = rupiah.format(0)
        txtTotalAngsuran.text = rupiah.format(0)
        monthlySimpanan = List(12) { 0.0 }
        monthlyAngsuran = List(12) { 0.0 }
        updateChart()
    }

    private fun updateChart() {
        val bulanLabel = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        val entriesS = monthlySimpanan.mapIndexed { i, v -> BarEntry(i.toFloat(), v.toFloat()) }
        val entriesA = monthlyAngsuran.mapIndexed { i, v -> BarEntry(i.toFloat(), v.toFloat()) }

        val dsS = BarDataSet(entriesS, "Simpanan").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            valueTextSize = 9f
        }
        val dsA = BarDataSet(entriesA, "Angsuran").apply {
            color = ColorTemplate.MATERIAL_COLORS[1]
            valueTextSize = 9f
        }

        val barData = BarData(dsS, dsA)
        barData.barWidth = 0.3f
        chartRekapKeuangan.data = barData

        val xAxis = chartRekapKeuangan.xAxis
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(bulanLabel)
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawGridLines(false)

        chartRekapKeuangan.groupBars(0f, 0.4f, 0.05f)
        chartRekapKeuangan.invalidate()
    }
}
