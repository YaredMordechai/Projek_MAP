package com.example.projek_map.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.projek_map.R
import com.example.projek_map.data.DummyUserData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class LaporanFragment : Fragment() {

    private lateinit var chartRekapKeuangan: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_laporan, container, false)
        chartRekapKeuangan = view.findViewById(R.id.chartRekapKeuangan)

        // 🔹 Tombol kembali ke dashboard
        val btnBackDashboard = view.findViewById<ImageButton>(R.id.btnBackDashboard)
        btnBackDashboard?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
                .replace(R.id.fragmentContainer, DashboardFragment())
                .addToBackStack(null)
                .commit()
        }

        setupRekapChart()

        return view
    }

    private fun setupRekapChart() {
        val bulan = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        // 🔹 Contoh data dummy rekap per bulan (bisa diganti pakai DummyUserData nantinya)
        val simpanan = listOf(1500000, 1600000, 1700000, 2000000, 2300000, 2500000)
        val pinjaman = listOf(800000, 900000, 1100000, 1300000, 1400000, 1550000)

        val entriesSimpanan = simpanan.mapIndexed { i, v -> BarEntry(i.toFloat(), v.toFloat()) }
        val entriesPinjaman = pinjaman.mapIndexed { i, v -> BarEntry(i.toFloat(), v.toFloat()) }

        val dataSetSimpanan = BarDataSet(entriesSimpanan, "Total Simpanan").apply {
            color = ColorTemplate.MATERIAL_COLORS[0]
            valueTextSize = 10f
        }
        val dataSetPinjaman = BarDataSet(entriesPinjaman, "Total Pinjaman").apply {
            color = ColorTemplate.MATERIAL_COLORS[2]
            valueTextSize = 10f
        }

        val barData = BarData(dataSetSimpanan, dataSetPinjaman)
        barData.barWidth = 0.35f

        chartRekapKeuangan.data = barData

        val xAxis = chartRekapKeuangan.xAxis
        xAxis.valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(bulan)
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 10f
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

        chartRekapKeuangan.axisLeft.textSize = 10f
        chartRekapKeuangan.axisRight.isEnabled = false
        chartRekapKeuangan.description = Description().apply {
            text = "Rekap Keuangan Koperasi"
            textSize = 10f
        }

        chartRekapKeuangan.setFitBars(true)
        chartRekapKeuangan.groupBars(0f, 0.4f, 0.05f)
        chartRekapKeuangan.animateY(1200)
        chartRekapKeuangan.invalidate()
    }
}
