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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projek_map.R
import com.example.projek_map.data.LaporanRepository
import com.example.projek_map.utils.PrefManager
import com.example.projek_map.ui.viewmodels.LaporanViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
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

    private lateinit var viewModel: LaporanViewModel

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

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LaporanViewModel(LaporanRepository()) as T
            }
        })[LaporanViewModel::class.java]

        viewModel.data.observe(viewLifecycleOwner) { d ->
            if (d != null) {
                applyLaporanData(d)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                // kamu sebelumnya tidak pakai toast di file ini,
                // jadi aku tidak paksa toast biar struktur tetap.
                // (kalau mau, tinggal tambahin toast)
            }
        }

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

        spinnerBulan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                val bulan = position + 1
                loadLaporanFromApi(bulan, lastYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupChart() {
        val desc = Description()
        desc.text = "Rekap Keuangan Bulanan"
        chartRekapKeuangan.description = desc
        chartRekapKeuangan.setFitBars(true)
    }

    private fun loadLaporanFromApi(bulan: Int, tahun: Int) {
        val kp = kodePegawaiAktif ?: return
        viewModel.load(kp, bulan, tahun)
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyLaporanData(data: Any) {
        // NOTE:
        // Ini tetap mengikuti struktur kamu yang sebelumnya mem-parsing response body.
        // Karena model response "data" untuk laporan user tergantung ApiModels kamu,
        // aku keep fleksibel pakai reflection-ish (map-like) TANPA mengubah fitur.
        //
        // Kalau `data` kamu sebenarnya class kuat (misal LaporanUserData),
        // kamu bisa ganti castingnya secara spesifik.

        try {
            // Kasus paling umum: data berupa Map<String, Any>
            if (data is Map<*, *>) {
                val totalSimpanan = (data["totalSimpanan"] as? Number)?.toDouble() ?: 0.0
                val totalPinjaman = (data["totalPinjaman"] as? Number)?.toDouble() ?: 0.0
                val totalAngsuran = (data["totalAngsuran"] as? Number)?.toDouble() ?: 0.0

                val simpananMonthly = (data["monthlySimpanan"] as? List<*>)?.map { (it as? Number)?.toDouble() ?: 0.0 } ?: List(12) { 0.0 }
                val angsuranMonthly = (data["monthlyAngsuran"] as? List<*>)?.map { (it as? Number)?.toDouble() ?: 0.0 } ?: List(12) { 0.0 }

                monthlySimpanan = simpananMonthly
                monthlyAngsuran = angsuranMonthly

                txtTotalSimpanan.text = "Total Simpanan: ${rupiah.format(totalSimpanan)}"
                txtTotalPinjaman.text = "Total Pinjaman: ${rupiah.format(totalPinjaman)}"
                txtTotalAngsuran.text = "Total Angsuran: ${rupiah.format(totalAngsuran)}"

                renderChart()
                return
            }

            // Fallback: kalau data adalah object class, coba ambil field via reflection
            val cls = data::class.java
            val fTotalSimpanan = cls.getDeclaredField("totalSimpanan").apply { isAccessible = true }
            val fTotalPinjaman = cls.getDeclaredField("totalPinjaman").apply { isAccessible = true }
            val fTotalAngsuran = cls.getDeclaredField("totalAngsuran").apply { isAccessible = true }
            val fMonthlySimpanan = cls.getDeclaredField("monthlySimpanan").apply { isAccessible = true }
            val fMonthlyAngsuran = cls.getDeclaredField("monthlyAngsuran").apply { isAccessible = true }

            val totalSimpanan = (fTotalSimpanan.get(data) as? Number)?.toDouble() ?: 0.0
            val totalPinjaman = (fTotalPinjaman.get(data) as? Number)?.toDouble() ?: 0.0
            val totalAngsuran = (fTotalAngsuran.get(data) as? Number)?.toDouble() ?: 0.0

            monthlySimpanan = (fMonthlySimpanan.get(data) as? List<*>)?.map { (it as? Number)?.toDouble() ?: 0.0 } ?: List(12) { 0.0 }
            monthlyAngsuran = (fMonthlyAngsuran.get(data) as? List<*>)?.map { (it as? Number)?.toDouble() ?: 0.0 } ?: List(12) { 0.0 }

            txtTotalSimpanan.text = "Total Simpanan: ${rupiah.format(totalSimpanan)}"
            txtTotalPinjaman.text = "Total Pinjaman: ${rupiah.format(totalPinjaman)}"
            txtTotalAngsuran.text = "Total Angsuran: ${rupiah.format(totalAngsuran)}"

            renderChart()
        } catch (_: Exception) {
            // biar tidak crash
        }
    }

    private fun renderChart() {
        val entries = ArrayList<BarEntry>()
        val idx = spinnerBulan.selectedItemPosition
        val s = monthlySimpanan.getOrNull(idx) ?: 0.0
        val a = monthlyAngsuran.getOrNull(idx) ?: 0.0

        entries.add(BarEntry(0f, s.toFloat()))
        entries.add(BarEntry(1f, a.toFloat()))

        val dataSet = BarDataSet(entries, "Rekap").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }
        val barData = BarData(dataSet)
        barData.barWidth = 0.5f

        chartRekapKeuangan.data = barData
        chartRekapKeuangan.invalidate()
    }
}
